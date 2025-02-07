package com.sprint.mission.service.jcf;


import com.sprint.mission.entity.User;
import com.sprint.mission.repository.jcf.JCFUserRepository;
import com.sprint.mission.service.dto.request.BinaryContentDto;
import com.sprint.mission.service.dto.request.CreateUserDto;
import com.sprint.mission.service.dto.request.UpdateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JCFUserService {

    private final JCFUserRepository userRepository;
    private final BinaryContentService binaryContentService;

    // 닉네임 중복 허용
    public User create(CreateUserDto userDto) {
        // DTO로 받고 생성
        // USERNAME, EMAIL 중복 허용 안함 <<<< 나중에
        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
        if (!(userDto.getProfileImg() == null)){
            binaryContentService.create(new B(user.getId(),));
        }
        // UserStatus도 같이 생성하라네
        //new UserStatus()
        return userRepository.save(user);
    }


    public User update(UUID userId, UpdateUserDto dto) {
        // DTO로 받고 업데이트
        //선택적으로 프로필 이미지를 대체할 수 있다
        User updatingUser = userRepository.findById(userId);
        updatingUser.setAll(dto.getUsername(), dto.getPassword(), dto.getEmail());
        return userRepository.save(updatingUser);
    }

    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    public User findById(UUID id) {
        return userRepository.findById(id);  // null 위험 없음
    }

    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    public List<User> findAll() {
        return userRepository.findAll();
    }


    //관련된 도메인도 같이 삭제 -> BinaryContent(프로필), Userstatus
    public void delete(User user) {
        userRepository.delete(user);
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
