package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.ResponseUserDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service // 서비스 Bean
@RequiredArgsConstructor // 생성자
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(UserDto userDto) {
        //username,email 중복 확인
        validationUsernameAndEmail(userDto.username(),userDto.email());

        //프로필 이미지 체크
        User user = binaryContentCheck(userDto);

        //UserStatus 생성
        UserStatus userStatus = new UserStatus(user,Instant.now(),user.getUserStatus().isOnlineStatus());
        userStatusRepository.save(userStatus);
        return userRepository.save(user);
    }
    //프로필 이미지 체크
    public User binaryContentCheck(UserDto userDto) {
        if(userDto.profileImage() == null) {
            return new User(userDto.username(), userDto.email(), userDto.password(), userDto.phoneNumber(), userDto.address(),null);
        }else{
            return new User(userDto.username(), userDto.email(), userDto.password(), userDto.phoneNumber(), userDto.address(), userDto.profileImage());
        }
    }
    //username, email 다른 유저와 같은지 체크
    public void validationUsernameAndEmail(String username, String email) {
        User existingUserByUsername = userRepository.findByUsername(username)
                .orElseThrow(()-> new NoSuchElementException("User Username not found"));
        User existingUserByEmail = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User Email not found"));
        if(existingUserByUsername.getUsername().equals(username)) {
            throw new IllegalArgumentException("이미 사용 중인 username 입니다.");
        }
        if(existingUserByEmail.getEmail().equals(email)) {
            throw new IllegalArgumentException("이미 사용중인 email 입니다.");
        }
    }

    @Override
    public ResponseUserDto findById(UserDto userDto) {
        User user = userRepository.findById(userDto.userId())
                .orElseThrow(()->new NoSuchElementException("UserId not found"));
        UserStatus userStatus = userStatusRepository.findById(userDto.userId())
                .orElseThrow(()-> new NoSuchElementException("userId not found"));
        return new ResponseUserDto(
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                userStatus.isOnlineStatus()
        );
    }

    @Override
    public List<ResponseUserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserStatus userStatus = userStatusRepository.findById(user.getId()).orElseThrow(()-> new NoSuchElementException("userId not found"));

            return new ResponseUserDto(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getAddress(),
                    userStatus.isOnlineStatus()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public User update(UserDto userDto) {
        User user = userRepository.findById(userDto.userId())
                .orElseThrow(()->new NoSuchElementException("UserId not found"));
        //기존 프로필 이미지 삭제
        if(userDto.profileImage() != null){
            if(user.getProfileImage() != null) {
                binaryContentRepository.deleteById(user.getProfileImage().getId());
            }
        }

        user.update(
                userDto.username(),
                userDto.email(),
                userDto.password(),
                userDto.phoneNumber(),
                userDto.address(),
                userDto.profileImage()
        );

        return userRepository.save(user);
    }

    @Override
    public void deleteById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new NoSuchElementException("userId not found"));
        //관련된 프로필 이미지 삭제
        if(user.getProfileImage() != null){
            binaryContentRepository.deleteById(user.getProfileImage().getId());
        }
        //UserStatus 삭제
        if(user.getUserStatus() != null){
            userStatusRepository.deleteById(user.getUserStatus().getId());
        }
        //유저 삭제
        userRepository.deleteById(user.getId());
    }
}
