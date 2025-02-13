package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.entity.AccountStatus;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(String nickname, String email, String password) {
        boolean userExists = userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
        if (userExists) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_REGISTERED);
        }

        UserStatus userStatus = new UserStatus();
        User newUser = new User(nickname, email, password, userStatus.getId(), null, AccountStatus.UNVERIFIED);
        return userRepository.save(newUser);
    }

    public User create(String nickname, String email, String password, BinaryContent profileImage) {
        User user = create(nickname, email, password);
        UserStatus userStatus = new UserStatus();
        user.setUserStatus(userStatus.getId());
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String userId) throws CustomException {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }
        return user;
    }

    @Override
    public User findByEmail(String email) throws CustomException {
        User user = userRepository.findAll().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with email %s not found", email));
        }
        return user;
    }

    @Override
    public List<User> findAllContainsNickname(String nickname) {
        return userRepository.findAll().stream()
                .filter(user -> user.getNickname().contains(nickname))
                .toList();
    }

    @Override
    public List<User> findAllByAccountStatus(AccountStatus accountStatus) {
        return userRepository.findAll().stream()
                .filter(user -> user.getAccountStatus().equals(accountStatus))
                .toList();
    }

    //UserStatus 필드 변경으로 인한 임시 주석 처리
//    @Override
//    public List<User> getUserByUserStatus(UserStatus userStatus) {
//        return userRepository.findAll().stream().filter(user -> user.getUserStatus().equals(userStatus)).toList();
//    }

    @Override
    public User updateUser(String userId, UpdateUserDto updateUserDto) throws CustomException {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        } else if (updateUserDto == null) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "USER DTO is null");
        }

        if (user.isUpdated(updateUserDto)) {
            user.setUpdatedAt(updateUserDto.updatedAt());
        }

        return userRepository.save(user);
    }

    // 선택적으로 프로필 이미지를 대체할 수 있도록 하는 메서드
    public User updateUser(String userId, UpdateUserDto updateUserDto, BinaryContent profileImg) throws CustomException {
        User user = updateUser(userId, updateUserDto);

        if(user.getProfileImageId() == null || user.getProfileImageId().isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA);
        }

        binaryContentService.deleteById(user.getProfileImageId());

        user.setProfileImageId(binaryContentService.create(profileImg).getId()); //todo - CreateBinaryContentDto로 변경되면 이것도 바꿔야 할 듯

        return userRepository.save(user);
    }


    @Override
    public boolean deleteUser(String userId) throws CustomException {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }
        return userRepository.delete(user);
    }
}
