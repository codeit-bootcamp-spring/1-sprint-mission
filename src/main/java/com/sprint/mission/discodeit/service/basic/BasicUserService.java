package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository repository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserDto createUser(UserDto paramUserDto) {
        UserDto userDto = vaildateUser(paramUserDto.userName(), paramUserDto.email());
        if (userDto != null) return userDto;
        User savedUser = repository.save(paramUserDto.userName(), paramUserDto.password(), paramUserDto.email());
        UserStatus savedUserStatus = userStatusRepository.save(new UserStatusDto(savedUser.getId()));
        if( paramUserDto.binaryContent() != null ) { //User 당 하나의 프로필 가짐
            BinaryContent savedBinaryContent = binaryContentRepository.save(new BinaryContentDto(savedUser.getId(), paramUserDto.binaryContent().getFile()));
        }
        return new UserDto(savedUser);
    }

    private UserDto vaildateUser(String userName, String email) {
        List<User> users = repository.findAll();
        if (users.stream().anyMatch(u -> u.getUserName().equals(userName))) {
            System.out.println("이미 존재하는 사용자입니다.");
            User user = users.stream().filter(s -> s.getUserName().equals(userName)).findAny().get();
            return new UserDto(user);
        }
        if (users.stream().anyMatch(u -> u.getEmail().equals(email))) {
            System.out.println("이미 존재하는 이메일입니다.");
            User user = users.stream().filter(s -> s.getEmail().equals(email)).findAny().get();
            return new UserDto(user);
        }
        return null;
    }

    @Override
    public UserDto getUser(UUID id) {
        return new UserDto(repository.findUserById(id));
    }

    @Override
    public List<UserDto> getUsers() {
        return repository.findAll().stream().map(UserDto::new).toList();
    }

    @Override
    public void updateUser(UserDto userDto) {
        repository.update(userDto.id(), userDto.userName(), userDto.email());
        if (userDto.binaryContent() != null) {
            System.out.println("profile 수정");
            binaryContentRepository.update(userDto.id(), userDto.binaryContent());
        }
    }

    @Override
    public void deleteUser(UUID id) {
        //id 검증 service단에서 실행
        repository.delete(id);
        binaryContentRepository.delete(id);
        userStatusRepository.delete(id);
        //구현체 구현에 따른 추가 로직 점검 필요
    }
}
