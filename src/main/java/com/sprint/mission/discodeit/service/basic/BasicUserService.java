package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User create(UserDTO.createDTO userCreateDto) {
        if (!isValidEmail(userCreateDto.email())) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        if (!userRepository.existsName(userCreateDto.userName())){
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        if(!userRepository.existsEmail(userCreateDto.email())){
            throw new IllegalArgumentException("이미 존재하는 사용자 이메일입니다.");
        }

        User user = new User(userCreateDto.userName(), userCreateDto.email(), userCreateDto.password());
        userRepository.save(user);

        return user;
    }

    @Override
    public UserDTO.responseDTO find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId());

        return new UserDTO.responseDTO(user.getId(), user.getUserName(), user.getEmail(), userStatus.isUserOnline());
    }

    @Override
    public List<UserDTO.responseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO.responseDTO(user.getId(), user.getUserName(), user.getEmail(), user.getOnlineStatus()))
                .toList();
    }

    @Override
    public User update(UserDTO.updateDTO userUpdateDTO) {
        User user = userRepository.findById(userUpdateDTO.userId())
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));

        if (!isValidEmail(userUpdateDTO.newEmail())) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        if (!userRepository.existsName(userUpdateDTO.newUsername())){
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        if(!userRepository.existsEmail(userUpdateDTO.newEmail())){
            throw new IllegalArgumentException("이미 존재하는 사용자 이메일입니다.");
        }

        user.update(userUpdateDTO.newUsername(), userUpdateDTO.newEmail());
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsId(userId)) {
            throw new NoSuchElementException("해당 유저가 없습니다.");
        }

        binaryContentRepository.delete(userId);
        userStatusRepository.delete(userId);

        userRepository.delete(userId);
    }

    private boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

}
