package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.web.dto.CheckUserDto;
import com.sprint.mission.discodeit.web.dto.UserUpdateDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class FileUserService implements UserService {

    private final UserRepository fileUserRespository;
    private final UserStatusRepository userStatusRepository;

    public FileUserService(UserRepository fileUserRespository, UserStatusRepository userStatusRepository) {
        this.fileUserRespository = fileUserRespository;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public User createUser(User user) {
        fileUserRespository.createUser(user);
        return user;
    }

    @Override
    public Optional<CheckUserDto> findUser(UUID id) {
        return null;
    }

    @Override
    public Optional<CheckUserDto> findByloginId(String loginId) {
        return null;
    }

    @Override
    public void updateProfile(UUID id, BinaryContent newProfile) {

    }

    @Override
    public List<CheckUserDto> findAllUsers() {
       return null;
    }

    @Override
    public void updateUser(UUID id, UserUpdateDto userParam) {
        validateFileUserExits(id);
        fileUserRespository.updateUser(id,userParam);
    }

    @Override
    public void deleteUser(UUID id) {
        validateFileUserExits(id);
        fileUserRespository.deleteUser(id);
    }

    @Override
    public void updateuserStatus(UUID userId) {
        validateFileUserExits(userId);
        isUserOnline(userId);
    }

    private void validateFileUserExits(UUID uuid) {
        if (!fileUserRespository.findById(uuid).isPresent()) {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
    private boolean isUserOnline(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return userStatus.isOnline();
    }
}
