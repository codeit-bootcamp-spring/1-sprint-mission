package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
//
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//
import java.time.Instant;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    // 파일을 저장하는 로직이 존재한다면, Service를 거쳐야 한다
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;
    private final InputHandler inputHandler;

    @Override
    public UserCreateResponse createUser(UserCreateRequest userCreateRequest, BinaryContentCreateRequest binaryContentCreateRequest) {
        // username과 email이 다른 유저와 같이 겹치는지 검증
        for(User user : userRepository.getAllUsers()){
            if(userCreateRequest.username().equals(user.getUsername())){
                throw new IllegalArgumentException("동일한 username이 존재합니다.");
            }
            if(userCreateRequest.email().equals(user.getEmail())){
                throw new IllegalArgumentException("동일한 email이 존재합니다.");
            }
        }

        // binaryContentService를 통해 binaryContent 도메인 객체 생성
        UUID profileImgId = null;
        if(binaryContentCreateRequest != null) {
            profileImgId = binaryContentService.createBinaryContent(binaryContentCreateRequest).getId();
        }

        // user 도메인 객체 생성
        User user = new User(
                userCreateRequest.username(),
                userCreateRequest.email(),
                userCreateRequest.password(),
                profileImgId);
        userRepository.saveUser(user);

        UserStatusCreateRequest userStatusCreateRequest = new UserStatusCreateRequest(user.getId(), Instant.now());
        // UserStatus 도메인 객체 생성
        UserStatus userStatus = userStatusService.createUserStatus(userStatusCreateRequest);

        // 객체 -> DTO 변환, 사용자에게 보여줄 것만
        return new UserCreateResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                userStatus.getId(),
                profileImgId
        );
    }

    @Override
    public Collection<UserFindResponse> showAllUsers() {
        // 전체 유저 조희
        if (userRepository.getAllUsers().isEmpty()) {
            // 예외 처리 했습니다!
            throw new RuntimeException("No Users");
        }else{
            return userRepository.getAllUsers().stream()
                    .map(user -> new UserFindResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getCreatedAt(),
                            user.getUpdatedAt(),
                            user.getProfileId(),
                            userStatusService.findUserStatusById(user.getId()).isConnectedNow()
                    )).collect(Collectors.toList());
        }
    }

    @Override
    public UserFindResponse getUserById(UUID id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(()-> new NoSuchElementException("해당 유저가 없습니다."));

        return new UserFindResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfileId(),
                userStatusService.findUserStatusByUserId(user.getId()).isConnectedNow()
        );
    }

    // Entity 속성값을 바꾸기 위해 검증할 땐 서비스 레이어에서 검증하는 게 일반적인가요,
    // 아니면 Entity에서 검증하는게 더 일반적인가요?
    // 지금은 이중으로 검증이 구현돼 있습니다.
    @Override
    public void updateUserInfo(UUID userId, UserUpdateRequest userUpdateRequest) {
        boolean isUpdated = false;
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾지 못했습니다."));

        if(userUpdateRequest.newUsername() != null){
            user.updateUsername(userUpdateRequest.newUsername());
            isUpdated = true;
        }
        if(userUpdateRequest.newEmail() != null){
            user.updateUserEmail(userUpdateRequest.newEmail());
            isUpdated = true;
        }
        if(userUpdateRequest.newPassword() != null){
            user.updateUserPassword(userUpdateRequest.newPassword());
            isUpdated = true;
        }

        if (userUpdateRequest.binaryContent() != null) {
            user.updateProfileId(userUpdateRequest.binaryContent().getId());
            isUpdated = true;
        }

        if(isUpdated) {
            userRepository.saveUser(user);
        }
    }

    @Override
    public void removeUserById(UUID id) {
        String keyword = inputHandler.getYesNOInput();
        if(keyword.equalsIgnoreCase("y")){
            // userStatus 를 불러오기 위해 user 을 불러온다. -비효율적인가?
            User user = userRepository.findUserById(id)
                            .orElseThrow(()-> new NoSuchElementException("유저가 없습니다,"));
            userStatusService.delteUserStatusByUserId(user.getId());
            binaryContentService.deleteBinaryContentById(user.getProfileId());
            userRepository.deleteUserById(id);
        }
    }
}
