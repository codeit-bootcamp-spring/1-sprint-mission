package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    private final BinaryContentService binaryContentService;
    private final UserValidator userValidator;


    @Override
    public UUID create(UserCreateDTO dto) {
        userValidator.validateUser(dto.getUsername(), dto.getEmail(), dto.password);
        User user = new User(dto.getUsername(), dto.getEmail(), dto.getPassword());

        if (dto.getFile() != null) {
            BinaryContentCreateDTO binaryContentCreateDTO = new BinaryContentCreateDTO(dto.getFile());
            UUID binaryContentId = binaryContentService.create(binaryContentCreateDTO);
            user.updateBinaryContentId(binaryContentId);
        }

        UUID userId = userRepository.save(user);
        userStatusRepository.save(new UserStatus(userId));
        return userId;
    }

    @Override
    public UserFindDTO find(UUID id) {
        User findUser = userRepository.findOne(id);
        Optional.ofNullable(findUser)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
        return toDTO(findUser);
    }

    @Override
    public List<UserFindDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toDTO).toList();
    }

    @Override
    public User update(UUID id, UserUpdateDTO dto) {
        userValidator.validateUpdateUser(id, dto.getName(), dto.getEmail(), dto.getPassword());
        User findUser = userRepository.findOne(id);
        findUser.setUser(dto.getName(), dto.getEmail(), dto.getPassword());

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
    public UUID delete(UUID id) {
        User findUser = userRepository.findOne(id);
        Optional.ofNullable(findUser)
                        .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));

        userStatusRepository.deleteByUserId(id);

        Optional.ofNullable(findUser.getBinaryContentId())
                        .ifPresent(binaryContentService::delete);

        return userRepository.delete(findUser.getId());
    }

    private UserFindDTO toDTO(User user){
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(null);

        return new UserFindDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                online,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}