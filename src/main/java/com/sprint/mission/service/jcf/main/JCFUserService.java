package com.sprint.mission.service.jcf.main;


import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.entity.UserStatus;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.dto.request.BinaryContentDto;
import com.sprint.mission.service.dto.request.UserDtoForCreate;
import com.sprint.mission.service.dto.request.UserDtoForUpdate;
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
    public User create(UserDtoForCreate userDto) {
        // USERNAME, EMAIL 중복 허용 안함 <<<< 나중에
        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());

        // 선택적 프로필 생성
        if (!(userDto.getProfileImg() == null)){
            profileService.create(new BinaryContentDto(user.getId(), userDto.getProfileImg()));
        }

        // UserStatus 생성
        userStatusService.create(user.getId());

        return userRepository.save(user);
    }


    public User update(UserDtoForUpdate dto) {
        User updatingUser = userRepository.findById(dto.getUserId());
        updatingUser.setAll(dto.getUsername(), dto.getPassword(), dto.getEmail());

        // 선택적으로 프로필 이미지를 대체할 수 있다
        if (dto.getProfileContent() != null){
            profileService.create(new BinaryContentDto(dto.getUserId(), dto.getProfileContent().getBytes()));
        }

        return userRepository.save(updatingUser);
    }

    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    public FindUserDto findById(UUID userId) {
        User findUser = userRepository.findById(userId);// null 위험 없음
        UserStatus status = userStatusService.findById(userId);
        return new FindUserDto(findUser,status.isOnline());
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

    public void joinChannel(User user, Channel channel){
        user.changeReadStatus(channel);
    }


//
//    @Override 닉네임 중복 허용 안할 시
//    public void validateDuplicateName(String name) {
//
//        if (!findUsersByName(name).isEmpty()) {
//            throw new DuplicateName(String.format("%s(은)는 이미 존재하는 닉네임입니다", name));
//        }
//    }
}
