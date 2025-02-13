package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
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
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
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
    public UUID createUser(UserCreateRequest userCreateRequest) {
        // username과 email이 다른 유저와 같이 겹치는지 검증
        for(User user : userRepository.getAllUsers()){
            if(userCreateRequest.username().equals(user.getUsername())){
                throw new IllegalArgumentException("동일한 username이 존재합니다.");
            }
            if(userCreateRequest.email().equals(user.getEmail())){
                throw new IllegalArgumentException("동일한 email이 존재합니다.");
            }
        }

        UserStatusCreateRequest userStatusCreateRequest = new UserStatusCreateRequest(Instant.now());
        // UserStatus 도메인 객체 생성
        UserStatus userStatus = userStatusService.createUserStatus(userStatusCreateRequest);

        // user 도메인 객체 생성
        User user = new User(userCreateRequest.username(), userCreateRequest.email(), userCreateRequest.password(), userStatus.getId());
        userRepository.saveUser(user);

        // UserStatus 에 userId 넣기
        userStatus.setUserId(user.getId());

        //  UserStatus 도메인 객체 생성 -> user 도메인 객체 생성(UserStatus 객체 주입) ->  UserStatus 에 userId 넣기
        // : 이 단계를 거치는 데 괜찮은건지 모르겠습니다

        // binaryContentService를 통해 binaryContent 도메인 객체 생성
        String keyword = inputHandler.getYesNOInput();
        if(keyword.equalsIgnoreCase("y")) {
            BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(user.getId(), null);
            binaryContentService.createBinaryContent(binaryContentCreateRequest);
        }
        return user.getId();
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
                            user.getUserStatusId()
                    ))
                    .collect(Collectors.toList());
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
                user.getUserStatusId()
        );
    }

    @Override
    public void updateUserInfo(UserUpdateRequest userUpdateRequest) {
        boolean isUpdated = false;
        User user = userRepository.findUserById(userUpdateRequest.userId())
                .orElseThrow(() -> new NoSuchElementException("유저를 찾지 못했습니다."));
        // 예외 종류 좀 더 알아보기

        if(userUpdateRequest.newUsername() != null){
            user.setUsername(userUpdateRequest.newUsername());
            isUpdated = true;
        }
        if(userUpdateRequest.newEmail() != null){
            user.setUserEmail(userUpdateRequest.newEmail());
            isUpdated = true;
        }
        if(userUpdateRequest.newPassword() != null){
            user.setUserPassword(userUpdateRequest.newPassword());
            isUpdated = true;
        }

        if (userUpdateRequest.binaryContent() != null) {
            userUpdateRequest.binaryContent().saveUserProfileImage(user.getId());
            isUpdated = true;
        }

        // 수정 시간 업데이트를 위해
        if(isUpdated) {
            user.refreshUpdateAt();
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
            // UserStatus 삭제
            userStatusService.deleteUserStatusById(user.getUserStatusId());
            // User 삭제
            userRepository.deleteUserById(id);
            // BinaryContent(프로필) 삭제
            binaryContentService.deleteBinaryContentByUserId(user.getId());
        }
    }
}
