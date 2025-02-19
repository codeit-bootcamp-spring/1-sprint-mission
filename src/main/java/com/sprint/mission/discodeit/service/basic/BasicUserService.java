package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.UserService;
import com.sprint.mission.discodeit.service.Interface.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BinaryContentService binaryContentService;
    @Autowired
    private final UserStatusService userStatusService;
    @Autowired
    private final UserStatusRepository userStatusRepository;


    @Override
    public UserDto createUser(UserCreateRequestDto request)  {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        }
        if(userRepository.existsByName(request.getName())){
            throw new IllegalArgumentException("Name already in use");
        }
        UUID profileId=null;
        if(request.getProfileImage() != null) {
            BinaryContentCreateRequestDto binaryRequest=new BinaryContentCreateRequestDto(null,null, request.getProfileImage());
            BinaryContent binaryContent = binaryContentService.createProfile(binaryRequest);
            profileId=binaryContent.getId();
        }

        User user = new User(request.getName(),request.getEmail(),request.getPassword());
        userRepository.save(user);

        Instant now = Instant.now();
        UserStatusCreateRequestDto userStatusCreateRequestDto =new UserStatusCreateRequestDto(user.getId(),now);
        userStatusService.create(userStatusCreateRequestDto);

        return toDto(user);
    }

    @Override
    public UserDto getUserById(UUID id) {
        return userRepository.getUserById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User id : "+id+" not found"));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.getAllUsers();
    }


    @Override
    public UserDto updateUser(UUID userId, UserUpdateRequestDto request)  {
        User user=userRepository.getUserById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.update(request.getNewName(), request.getNewEmail(), request.getNewPassword());
        userRepository.save(user);

        if (request.getNewProfileImage() != null){
            binaryContentService.deleteByUserId(user.getId());
            BinaryContentCreateRequestDto binaryRequest=new BinaryContentCreateRequestDto(user.getId(), null,request.getNewProfileImage());
            binaryContentService.createProfile(binaryRequest);
        }

        return toDto(user);
    }

    @Override
    public void deleteUser(UUID id) {
        if(!userRepository.existsById(id)){
            throw new NoSuchElementException("User with id not found");
        }
        userRepository.deleteUser(id);
        userStatusService.deleteByUserId(id);
        binaryContentService.deleteByUserId(id);

    }

    private UserDto toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(false);

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfileId(),
                online
        );
    }
}
