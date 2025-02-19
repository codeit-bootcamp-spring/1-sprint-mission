package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserResponse;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.repository.UserStatusRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    @Override
    public User create(UserCreateRequest userCreateRequest) {
        // 파라미터 검증
        if (checkEmailDuplicate(userCreateRequest.getEmail())) {
            throw new IllegalArgumentException(userCreateRequest.getEmail() + " Email Already Exists");
        }
        if (checkNameDuplicate(userCreateRequest.getName())) {
            throw new IllegalArgumentException(userCreateRequest.getName() + "Name Already Exists");
        }
        // User 생성, 저장
        User user = new User(userCreateRequest.getName(),
                userCreateRequest.getEmail(), userCreateRequest.getPassword());
        userRepository.save(user);

        // 프로필 이미지 저장
        if(userCreateRequest.getProfileImage()!=null){
            BinaryContentCreate binaryContentDto = new BinaryContentCreate(user.getId(), null,
                    userCreateRequest.getProfileImage());
            binaryContentService.create(binaryContentDto);
        }
        // UserStatus 생성
        UserStatusCreate userStatusCreate = new UserStatusCreate(user.getId(), UserStatusType.ONLINE, Instant.now());
        userStatusService.create(userStatusCreate);

        return user;
    }

    @Override
    public UserResponse find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        UserStatus userStatus = userStatusService.findByUserId(userId);
        return new UserResponse(user, userStatus);
    }

    @Override
    public List<UserResponse> findAll() {
        Map<UUID, User> data = userRepository.findAll();
        return data.values().stream()
                .sorted(Comparator.comparing(user -> user.getCreatedAt()))
                .map(user->find(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public User update(UUID userId, UserUpdateRequest userUpdateRequest) {
        if (checkEmailDuplicate(userUpdateRequest.getEmail())) {
            throw new IllegalArgumentException(userUpdateRequest.getEmail() + " Email Already Exists");
        }
        if (checkNameDuplicate(userUpdateRequest.getName())) {
            throw new IllegalArgumentException(userUpdateRequest.getName() + "Name Already Exists");
        }
        //프로필 이미지 선택적 대체
        if(userUpdateRequest.getProfileImage()!=null){
            binaryContentService.deleteUserProfile(userId);
            binaryContentService.create(new BinaryContentCreate(userId, null, userUpdateRequest.getProfileImage()));
        }
        // User 객체 변경
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usre ID: " + userId + " Not found"));
        user.update(userUpdateRequest.getName(), userUpdateRequest.getEmail(),
                userUpdateRequest.getPassword());
        userRepository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        // 존재하는 유저인지 검증
        if (userRepository.existsById(userId)) {
            userRepository.delete(userId);
            // 유저의 프로필  BinaryContent 삭제
            binaryContentService.deleteUserProfile(userId);
            // UserStatus 삭제
            userStatusService.deleteByUserId(userId);
        } else {
            throw new NoSuchElementException("User ID: " + userId + " Not Found");
        }
    }


    public boolean checkEmailDuplicate(String email) {
        Map<UUID, User> users = userRepository.findAll();
        if (users == null || users.isEmpty()) {
            return false;
        }
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean checkNameDuplicate(String name) {
        Map<UUID, User> users = userRepository.findAll();
        if (users == null || users.isEmpty()) {
            return false;
        }
        return users.values().stream()
                .anyMatch(user -> user.getName().equals(name));
    }

}
