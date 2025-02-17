package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContent.CreateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserIdDTO;
import com.sprint.mission.discodeit.dto.user.CreatedUserDataDTO;
import com.sprint.mission.discodeit.dto.user.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.data.BinaryContent;
import com.sprint.mission.discodeit.entity.data.ContentType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final BinaryContentService binaryContentService;

    @Override
    public User createUser(CreatedUserDataDTO data, BinaryContentDTO proFile) {
        try {
            User user = null;
            userValidator.validateUser(data.name(), data.email(), data.password());
            if(proFile != null){
                CreateDTO createDTO = new CreateDTO(
                        ContentType.PROFILE_IMAGE,
                        proFile.filename(),
                        proFile.fileType(),
                        proFile.data()
                );
                BinaryContent content = binaryContentService.created(createDTO);
                user = new User(data.name(), data.email(), data.id(), data.password(), content.getId());
            }else{
                user = new User(data.name(), data.email(), data.id(), data.password());
            }

            userRepository.save(user);
            UserIdDTO statusDTO = new UserIdDTO(user.getId());
            userStatusService.created(statusDTO);

            return user;
        }catch (CustomException e){
            System.out.println("유저생성 실패 -> " + e.getMessage());
            return null;
        }
    }

    public User createUser(CreatedUserDataDTO data){
        return createUser(data, null);
    }


    @Override
    public User find(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        user.update(newUsername, newEmail, newPassword);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }
}
