package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.UserService;
import com.sprint.mission.discodeit.service.Interface.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private BinaryContentService binaryContentService;
    @Autowired
    private UserStatusService userStatusService;


    @Override
    public User createUser(UserCreateRequestDto request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        }
        if(userRepository.existsByName(request.getName())){
            throw new IllegalArgumentException("Name already in use");
        }
        User user = new User(request.getName(),request.getEmail(),request.getPassword());
        userRepository.save(user);

        UserStatusRequest userStatusRequest=new UserStatusRequest(user.getId(), request.getStatus());
        userStatusService.create(userStatusRequest);

        if(request.getProfileImage() != null) {
            BinaryContentCreateRequestDto binaryRequest=new BinaryContentCreateRequestDto(user.getId(), request.getProfileImage());
            binaryContentService.createProfile(binaryRequest);
        }
        return user;
    }

    @Override
    public Optional<UserResponseDto> getUserById(UUID id) {
        Optional<User> userOptional = userRepository.getUserById(id);

        // 조회된 사용자가 존재하면 UserResponseDTO로 변환
        return userOptional.map(user -> {
            String userStatus = user.getUserStatus();
            Boolean isOnline = Optional.ofNullable(userStatus)
                    .map((String t) -> UserStatus.isOnline())
                    .orElse(false);

            // UserResponseDTO 생성 후 반환
            return new UserResponseDto(user.getName(), user.getEmail(), isOnline);
        });
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(user -> {
                    String userStatus=user.getUserStatus();
                    boolean isOline=Optional.ofNullable(userStatus)
                            .map((String t) -> UserStatus.isOnline())
                            .orElse(false);
                    return new UserResponseDto(user.getName(),user.getEmail(),isOline);
                }).collect(Collectors.toList());
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.getAllUsers();
    }


    @Override
    public User updateUser(UserUpdateRequestDto request) {
        User user=userRepository.getUserById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not fount"));
        user.update(request.getNewName(), request.getNewEmail(), request.getNewPassword());
        userRepository.save(user);

        if (request.getNewProfileImage() != null){
            binaryContentService.deleteByUserId(user.getId());
            BinaryContentCreateRequestDto binaryRequest=new BinaryContentCreateRequestDto(user.getId(), request.getNewProfileImage());
            binaryContentService.createProfile(binaryRequest);
        }

        return user;
    }

    @Override
    public void deleteUser(UUID id) {
        if(!userRepository.existsById(id)){
            throw new NoSuchElementException("User with id not fount");
        }
        userRepository.deleteUser(id);
        userStatusService.deleteByUserId(id);
        binaryContentService.deleteByUserId(id);
    }

}
