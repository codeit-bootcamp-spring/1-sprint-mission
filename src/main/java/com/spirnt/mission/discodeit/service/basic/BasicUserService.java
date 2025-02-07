package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserResponse;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private ChannelService channelService;
    private MessageService messageService;
    private BinaryContentService binaryContentService;
    private UserStatusService userStatusService;


    @Override
    public void setService(ChannelService channelService, MessageService messageService,
                           BinaryContentService binaryContentService, UserStatusService userStatusService) {
        this.channelService = channelService;
        this.messageService = messageService;
        this.binaryContentService = binaryContentService;
        this.userStatusService = userStatusService;
    }


    @Override
    public User create(UserCreateRequest dto) {
        if (checkEmailDuplicate(dto.getEmail())) {
            throw new IllegalArgumentException(dto.getEmail() + " Email Already Exists");
        }
        if (checkNameDuplicate(dto.getName())) {
            throw new IllegalArgumentException(dto.getName() + "Name Already Exists");
        }
        User user = new User(dto);
        //프로필 이미지 저장
        if (dto.getProfileImage() != null)
            binaryContentService.create(new BinaryContentCreate(user.getId(), null, dto.getProfileImage()));
        userRepository.save(user);
        // UserStatus 생성
        UserStatusCreate userStatusCreate = new UserStatusCreate(user.getId(), UserStatusType.ONLINE, Instant.now());
        userStatusService.create(userStatusCreate);
        return user;
    }

    @Override
    public UserResponse find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        return new UserResponse(user);
    }

    @Override
    public List<UserResponse> findAll() {
        Map<UUID, User> data = userRepository.findAll();
        List<UserResponse> list = new ArrayList<>();
        if (data.isEmpty()) return list;
        data.values().stream()
                .sorted(Comparator.comparing(user -> user.getCreatedAt()))
                .forEach(user -> {
                    list.add(new UserResponse(user));
                });
        return list;
    }

    @Override
    public User update(UUID userId, UserUpdateRequest dto) {
        if (checkEmailDuplicate(dto.getEmail())) {
            throw new IllegalArgumentException(dto.getEmail() + " Email Already Exists");
        }
        if (checkNameDuplicate(dto.getName())) {
            throw new IllegalArgumentException(dto.getName() + "Name Already Exists");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usre ID: " + userId + " Not found"));
        user.update(dto);
        userRepository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        // 존재하는 유저인지 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        // 해당 유저가 속한 모든 채널에서 삭제하기 위해 채널 서비스 호출
        channelService.deleteUserInAllChannels(userId);
        // 유저의 프로필 삭제

        // UserStatus 삭제
        userStatusService.deleteByUserId(userId);
        userRepository.delete(userId);
    }


    @Override
    public boolean checkEmailDuplicate(String email) {
        Map<UUID, User> users = userRepository.findAll();
        if (users == null || users.isEmpty()) {
            return false;
        }
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean checkNameDuplicate(String name) {
        Map<UUID, User> users = userRepository.findAll();
        if (users == null || users.isEmpty()) {
            return false;
        }
        return users.values().stream()
                .anyMatch(user -> user.getName().equals(name));
    }

}
