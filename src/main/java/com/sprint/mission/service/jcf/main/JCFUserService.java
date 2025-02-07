package com.sprint.mission.service.jcf.main;


import com.sprint.mission.entity.*;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.dto.request.BinaryContentDto;
import com.sprint.mission.service.dto.request.UserDtoForRequest;
import com.sprint.mission.service.dto.response.FindUserDto;
import com.sprint.mission.service.jcf.addOn.BinaryProfileService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JCFUserService {

    private final JCFUserRepository userRepository;
    private final BinaryProfileService profileService;
    private final UserStatusService userStatusService;

    // 닉네임 중복 허용
    public void create(UserDtoForRequest userDto) {
        isDuplicateNameEmail(userDto);

        User user = User.createUserByDto(userDto);

        // 선택적 프로필 생성
        BinaryContent profileImg = userDto.getProfileImg();
        if (!(profileImg == null)) profileService.create(new BinaryContentDto(user.getId(), profileImg));

        // UserStatus 생성
        userStatusService.create(user.getId());

        userRepository.save(user);
    }

    public void update(UUID userId, UserDtoForRequest dto) {
        isDuplicateNameEmail(dto);

        User updatingUser = userRepository.findById(userId);
        updatingUser.setAll(dto.getUsername(), dto.getPassword(), dto.getEmail());

        // 선택적으로 프로필 이미지를 대체할 수 있다
        if (dto.getProfileImg() != null) profileService.create(new BinaryContentDto(userId, dto.getProfileImg()));
        // User가 프로필을 안 갖고 있다

        userRepository.save(updatingUser);
    }

    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    public FindUserDto findById(UUID userId) {
        User findUser = userRepository.findById(userId);// null 위험 없음
        UserStatus status = userStatusService.findById(userId);
        return new FindUserDto(findUser, status.isOnline());
    }

    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    public List<FindUserDto> findAll() {
        List<User> allUser = userRepository.findAll();
        List<FindUserDto> findUserDtos = new ArrayList<>();
        for (User user : allUser) {
            UserStatus userStatus = userStatusService.findById(user.getId());
            findUserDtos.add(new FindUserDto(user, userStatus.isOnline()));
        }
        return findUserDtos;
    }

    //관련된 도메인도 같이 삭제 -> BinaryContent(프로필), Userstatus
    public void delete(User user) {
        userRepository.delete(user);
        userStatusService.delete(user.getId());
        profileService.delete(user.getId());
    }

    // 온라인 / 오프라인 메서드 나중에

    public void joinChannel(UUID userId, UUID channelId) {
        User joiningUser = userRepository.findById(userId);
        joiningUser.changeReadStatus(channelId);
        //사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현
    }

    public void outChannel(UUID userId, UUID channelId) {}

    private void isDuplicateNameEmail(UserDtoForRequest userDto) {
        List<User> allUser = userRepository.findAll();
        boolean isDuplicateName = allUser.stream().anyMatch(user -> user.getName().equals(userDto.getUsername()));
        if (isDuplicateName) throw new IllegalStateException(String.format("Duplicate Name: %s", userDto.getUsername()));

        boolean isDuplicateEmail = allUser.stream().anyMatch(user -> user.getEmail().equals(userDto.getEmail()));
        if (isDuplicateEmail) throw new IllegalStateException(String.format("Duplicate Email: %s", userDto.getUsername()));
    }
}
