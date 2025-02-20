package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UpdateUserDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasicUserService implements UserService {
    @Autowired
    private final UserRepository userRepository;
    public BasicUserService(UserRepository userRepository){this.userRepository = userRepository;}
    @Autowired
    private final BinaryContentRepository binaryContentRepository;
    public BasicUserService(BinaryContentRepository binaryContentRepository){this.binaryContentRepository = binaryContentRepository;}
    @Autowired
    private final UserStatusRepository userStatusRepository;
    public BasicUserService(UserStatusRepository userStatusRepository){this.userStatusRepository = userStatusRepository;}


    @Override
    public boolean createUser(UserDto userDto) {
        // TODO : username과 email은 다른 유저와 같으면 안되는 거 처리 (validate..?)

        boolean created = userRepository.save(userDto);
        if(created){
            if(userDto.profileImageUrl() != null){
                binaryContentRepository.saveProfileImage(userDto.profileImageUrl(), userDto.user().getId());
            }
            System.out.println("생성된 회원: " + userDto);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public Optional<UserDto> findUser(UserDto userDto) {
        Optional<UserDto> usr = userRepository.findById(userDto.user().getId());
        usr.ifPresent(u -> System.out.println("조회된 회원: " + u));
        // TODO : 패스워드 정보 제외시키기
        return usr;
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<UserDto> users = userRepository.findAll();
        if(users != null && !users.isEmpty()){
            System.out.println("전체 회원 목록: " + users);
            return users;
        } else {
            System.out.println("회원 목록이 비어 있습니다.");
            return Collections.emptyList(); // 비어 있을 경우 빈 리스트 반환
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void updateUser(UpdateUserDto updateUserDto) {
        UUID id = updateUserDto.id();
        boolean updated = userRepository.updateOne(
                updateUserDto.id(),
                updateUserDto.name(),
                updateUserDto.age(),
                updateUserDto.gender()
        );

        if (updated) {
            if (updateUserDto.profileImageUrl() != null) {
                binaryContentRepository.saveProfileImage(updateUserDto.profileImageUrl(), id);
            }

            Optional<UserDto> usr = userRepository.findById(id);
            usr.ifPresent(u -> System.out.println("수정된 회원: " + u));
            List<UserDto> allUsers = userRepository.findAll();
            System.out.println("수정 후 전체 회원 목록: " + allUsers);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        // 관련 도메인 삭제
        binaryContentRepository.deleteAllByUserId(id);
        userStatusRepository.deleteAllByUserId(id);

        boolean deleted = userRepository.deleteOne(id);
        if (deleted) {
            Optional<UserDto> usr = userRepository.findById(id);
            System.out.println("삭제된 회원: " + usr);
            List<UserDto> allUsers = userRepository.findAll();
            System.out.println("삭제 후 전체 회원 목록: " + allUsers);
        }
    }

    // TODO : LoginDto로 AuthService 구현

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll();
    }
}