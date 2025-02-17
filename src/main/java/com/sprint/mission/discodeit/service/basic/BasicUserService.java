package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceFindDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceUpdateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;
    private final UserValidator userValidator;

    @Override
    public UUID create(UserServiceCreateDTO dto) {
        userValidator.validateUser(dto.getUsername(), dto.getEmail(), dto.password);

        User user = new User(dto.getUsername(), dto.getEmail(), dto.getPassword());

        UUID userId = userRepository.save(user);

        if (dto.getFile() != null) {
            BinaryContentCreateDTO binaryContentCreateDTO =
                    new BinaryContentCreateDTO(dto.getFile());
            UUID binaryContentId = binaryContentService.create(binaryContentCreateDTO);
            user.updateBinaryContentId(binaryContentId);
        }
        userStatusService.create(new UserStatusCreateDTO(userId));

        return userId;
    }

    @Override
    public UserServiceFindDTO find(UUID id) {
        User findUser = userRepository.findOne(id);
        Optional.ofNullable(findUser)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));

        UserStatus findUserStatus = userStatusService.findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(findUser.getId()))
                .findFirst().orElseThrow(() -> new NoSuchElementException("userId: "+findUser.getId()+"유저의 UsserStatus가 없습니다."));

        return new UserServiceFindDTO(
                findUser.getId(),
                findUser.getUsername(),
                findUser.getEmail(),
                findUserStatus.getType());
    }

    @Override
    public List<UserServiceFindDTO> findAll() {
        List<User> users = userRepository.findAll();

        Map<UUID, UserStatusType> map = userStatusService.findAll().stream()
                .collect(Collectors.toMap(UserStatus::getUserId, UserStatus::getType));

        return users.stream()
                .map(user -> new UserServiceFindDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        map.getOrDefault(user.getId(), null)
                )).toList();
    }

    @Override
    public User update(UserServiceUpdateDTO dto) {
        userValidator.validateUpdateUser(dto.getId(), dto.getName(), dto.getEmail());
        User findUser = userRepository.findOne(dto.getId());
        findUser.setUser(dto.getName(), dto.getEmail());

        //기존 사진이 있다면 삭제하고 만들기
        if(dto.getFile() != null){
            if(findUser.getBinaryContentId() !=null){
                binaryContentService.delete(findUser.getBinaryContentId());
            }
            UUID binaryContentId = binaryContentService.create(new BinaryContentCreateDTO(dto.getFile()));
            findUser.updateBinaryContentId(binaryContentId);
        }
        userRepository.update(findUser);
        return findUser;
    }

    @Override
    public UUID updateUserOnline(UUID userId, Instant time){
        User findUser = userRepository.findOne(userId);
        Optional.ofNullable(findUser)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
        userStatusService.updateByUserId(userId, time);
        return userId;
    }

    @Override
    public UUID delete(UUID id) {
        User findUser = userRepository.findOne(id);

        //userStatus 삭제
        userStatusService.findAll().stream()
                .filter(uS -> uS.getUserId().equals(findUser.getId()))
                .findFirst()
                .map(UserStatus::getId)
                .ifPresent(userStatusService::delete);

        //프로필 이미지 삭제
        binaryContentService.delete(findUser.getBinaryContentId());


        return userRepository.delete(findUser.getId());
    }

}