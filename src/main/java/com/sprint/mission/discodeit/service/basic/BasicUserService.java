package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDTO;
import com.sprint.mission.discodeit.dto.user.UserResponseDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.Interface.UserService;
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
    private final UserStatusRepository userStatusRepository;
    @Autowired
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public User createUser(UserCreateRequestDTO request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        }
        if(userRepository.existsByName(request.getName())){
            throw new IllegalArgumentException("Name already in use");
        }
        User user = new User(request.getName(),request.getEmail(),request.getPassword());
        userRepository.save(user);

        UserStatus status=new UserStatus(user.getId(),request.getStatus());
        userStatusRepository.save(status);

        if(request.getProfileImage() != null){
            BinaryContent profile=new BinaryContent(user.getId(),request.getProfileImage());
            binaryContentRepository.save(profile);
        }
        return user;
    }

    @Override
    public Optional<UserResponseDTO> getUserById(UUID id) {
        Optional<User> userOptional = userRepository.getUserById(id);

        // 조회된 사용자가 존재하면 UserResponseDTO로 변환
        return userOptional.map(user -> {
            String userStatus = user.getUserStatus();
            Boolean isOnline = Optional.ofNullable(userStatus)
                    .map((String t) -> UserStatus.isOnline())
                    .orElse(false);

            // UserResponseDTO 생성 후 반환
            return new UserResponseDTO(user.getName(), user.getEmail(), isOnline);
        });
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(user -> {
                    String userStatus=user.getUserStatus();
                    boolean isOline=Optional.ofNullable(userStatus)
                            .map((String t) -> UserStatus.isOnline())
                            .orElse(false);
                    return new UserResponseDTO(user.getName(),user.getEmail(),isOline);
                }).collect(Collectors.toList());
    }


    @Override
    public User updateUser(UserUpdateRequestDTO request) {
        User user=userRepository.getUserById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not fount"));
        user.update(request.getNewName(), request.getNewEmail(), request.getNewPassword());
        userRepository.save(user);

        if (request.getNewProfileImage() != null){
            binaryContentRepository.deleteByUserId(request.getUserId());
            BinaryContent newProfile=new BinaryContent(request.getUserId(),request.getNewProfileImage());
            binaryContentRepository.save(newProfile);
        }

        return user;
    }

    @Override
    public void deleteUser(UUID id) {
        if(!userRepository.existsById(id)){
            throw new NoSuchElementException("User with id not fount");
        }
        userRepository.deleteUser(id);
        userStatusRepository.deleteByUserId(id);
        binaryContentRepository.deleteByUserId(id);
    }

}
