package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.web.dto.CheckUserDto;
import com.sprint.mission.discodeit.web.dto.UserUpdateDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository contentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User createUser(User user) {
        List<User> all = userRepository.findAll();
        for (User findUser : all) {
            //동명이인이 있을수있는데 Username이 다른유저와 같으면 안된다는 요구사항이 필요한 요소일까요?
            if (user.getLoginId().equals(findUser.getLoginId())) {
                throw new IllegalArgumentException("중복된 아이디입니다.");
            }
            if (user.getUserEmail().equals(findUser.getUserEmail())) {
                throw new IllegalArgumentException("중복된 이메일 입니다.");
            }
        }
        if (user.getAttachProfile() != null) {
           contentRepository.save(user.getAttachProfile());
        }
        User savedUser = userRepository.createUser(user);
        UserStatus userStatus = new UserStatus(savedUser.getId());
        userStatusRepository.save(userStatus);
        return savedUser;
    }

    @Override
    public Optional<CheckUserDto> findUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        return Optional.of(new CheckUserDto(user, isUserOnline(id)));
    }

    @Override
    public Optional<CheckUserDto> findByloginId(String loginId) {
        User user = userRepository.findByloginId(loginId).orElseThrow(()->new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        return Optional.of(new CheckUserDto(user, isUserOnline(user.getId())));
    }

    @Override
    public List<CheckUserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user->{ UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElseThrow(()->new IllegalArgumentException("해당 사용자 상태정보를 찾을 수 없습니다."));
                    return new CheckUserDto(user, isUserOnline(user.getId()));
                }).collect(Collectors.toList());
    }

    @Override
    public void updateUser(UUID id, UserUpdateDto userParam) {
        validateUserExits(id);
        userRepository.updateUser(id, userParam);
    }
    @Override
    public void updateProfile(UUID id, BinaryContent newProfile) {
        validateUserExits(id);
        User user = findUserById(id);
        BinaryContent originalProfile = user.getAttachProfile();
        user.setAttachProfile(newProfile);
        contentRepository.deleteById(originalProfile.getId());
        contentRepository.save(newProfile);
        userRepository.createUser(user);//덮어쓰기
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }

    @Override
    public void deleteUser(UUID userId) {
        validateUserExits(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
        BinaryContent attachProfile = user.getAttachProfile();
        userStatusRepository.deleteByUserId(userId);
        if(attachProfile != null) {
            contentRepository.deleteById(attachProfile.getId());
        }
        userRepository.deleteUser(userId);
    }

    @Override
    public void updateuserStatus(UUID userId) {
        validateUserExits(userId);
        isUserOnline(userId);
    }

    private void validateUserExits(UUID uuid) {
        if (userRepository.findById(uuid).isEmpty()) {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }

    private boolean isUserOnline(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return userStatus.isOnline();
    }
}
