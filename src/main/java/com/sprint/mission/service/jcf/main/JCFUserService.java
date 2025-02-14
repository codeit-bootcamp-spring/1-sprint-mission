package com.sprint.mission.service.jcf.main;


import com.sprint.mission.entity.addOn.BinaryProfileContent;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.UserService;
import com.sprint.mission.dto.request.BinaryProfileContentDto;
import com.sprint.mission.dto.request.UserDtoForRequest;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.service.exception.NotFoundId;
import com.sprint.mission.service.jcf.addOn.BinaryProfileService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFUserService implements UserService {

    private final JCFUserRepository userRepository;
    private final BinaryProfileService profileService;
    private final UserStatusService userStatusService;

    // 테스트에서 create 후 userId 필요해서 임시적 USER 반환 (할거면 나중에 컨트롤러에서)
    @Override
    public void create(UserDtoForRequest userDto) {
        isDuplicateNameEmail(userDto.getUsername(), userDto.getEmail());
        User user = User.createUserByRequestDto(userDto);

        // 선택적 프로필 생성
        BinaryProfileContent profileImg = userDto.getProfileImg();
        if (!(profileImg == null)) profileService.create(new BinaryProfileContentDto(profileImg));

        // UserStatus 생성
        userStatusService.create(user.getId());

        userRepository.save(user);
    }


    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    @Override
    public void update(UUID userId, UserDtoForRequest updateDto) {
        isDuplicateNameEmail(updateDto.getUsername(), updateDto.getEmail());

        User updatingUser = userRepository.findById(userId).orElseThrow(NotFoundId::new);
        updatingUser.updateByRequestDTO(updateDto);

        if (updateDto.getProfileImg() != null) {
            profileService.create(new BinaryProfileContentDto(updateDto.getProfileImg()));
        }

        userRepository.save(updatingUser);

    }

    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(NotFoundId::new);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    //관련된 도메인도 같이 삭제 -> BinaryContent(프로필), Userstatus
    @Override
    public void delete(UUID userID) {
        if (!userRepository.existsById(userID)) return;

        userRepository.delete(userID);
        userStatusService.delete(userID);
        profileService.delete(userID);
    }

    // 온라인 / 오프라인 메서드 나중에


    public void joinChannel(UUID userId, UUID channelId) {
        userRepository.findById(userId).ifPresent((joiningUser) -> {
            joiningUser.changeReadStatus(channelId);
        });

        //사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현
    }

    public void outChannel(UUID userId, UUID channelId) {
    }

    @Override
    public void isDuplicateNameEmail(String name, String email) {
        List<User> allUser = userRepository.findAll();
        boolean isDuplicateName = allUser.stream().anyMatch(user -> user.getName().equals(name));
        if (isDuplicateName)
            throw new IllegalStateException(String.format("Duplicate Name: %s", name));

        boolean isDuplicateEmail = allUser.stream().anyMatch(user -> user.getEmail().equals(email));
        if (isDuplicateEmail)
            throw new IllegalStateException(String.format("Duplicate Email: %s", name));
    }
}
