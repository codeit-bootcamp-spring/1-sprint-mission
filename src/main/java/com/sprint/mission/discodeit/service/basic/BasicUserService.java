package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.entity.AccountStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final BinaryContentService binaryContentService;

    @Override
    public UserResponseDto create(CreateUserDto createUserDto) {
        boolean userExists = userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(createUserDto.email()));
        if (userExists) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_REGISTERED);
        }

        User user = new User(createUserDto.username(), createUserDto.nickname(), createUserDto.email(), createUserDto.password(), null, AccountStatus.UNVERIFIED, null);
        userRepository.save(user);

        UserStatus userStatus = userStatusService.create(new CreateUserStatusDto(user.getId()));

        return UserResponseDto.from(user, userStatus.isActive());
    }

    public UserResponseDto create(CreateUserDto createUserDto, String profileImageId) {
        UserResponseDto userDto = create(createUserDto);
        User user = userRepository.findById(userDto.id());
        user.setProfileImageId(profileImageId);
        userRepository.save(user);

        return UserResponseDto.from(user, userStatusService.findById(user.getId()).isActive());

    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map( u -> UserResponseDto.from(u, userStatusService.findById(u.getId()).isActive())).toList();
    }

    @Override
    public UserResponseDto findById(String userId) throws CustomException {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }
        return UserResponseDto.from(user, userStatusService.findById(user.getId()).isActive());
    }

    @Override
    public UserResponseDto findByEmail(String email) throws CustomException {
        User user = userRepository.findAll().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with email %s not found", email));
        }
        return UserResponseDto.from(user, userStatusService.findById(user.getId()).isActive());
    }

    @Override
    public List<UserResponseDto> findAllContainsNickname(String nickname) {
        return userRepository.findAll().stream()
                .filter(user -> user.getNickname().contains(nickname))
                .map(user->UserResponseDto.from(user, userStatusService.findById(user.getId()).isActive()))
                .toList();
    }

    @Override
    public List<UserResponseDto> findAllByAccountStatus(AccountStatus accountStatus) {
        return userRepository.findAll().stream()
                .filter(user -> user.getAccountStatus().equals(accountStatus))
                .map(user -> UserResponseDto.from(user, userStatusService.findById(user.getId()).isActive()))
                .toList();
    }

    //UserStatus 필드 변경으로 인한 임시 주석 처리
//    @Override
//    public List<User> getUserByUserStatus(UserStatus userStatus) {
//        return userRepository.findAll().stream().filter(user -> user.getUserStatus().equals(userStatus)).toList();
//    }


    //todo
    //update user의 반환타입을 Dto 로 변경하려 하였으나
    //아래랑 종속적이라... 문제가 됨 -> 수정필요
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
    public User updateUser(String userId, UpdateUserDto updateUserDto, CreateBinaryContentDto createBinaryContentDto) throws CustomException {
        User user = updateUser(userId, updateUserDto);

        if (user.getProfileImageId() == null || user.getProfileImageId().isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA);
        }

        binaryContentService.deleteById(user.getProfileImageId());

        user.setProfileImageId(binaryContentService.create(createBinaryContentDto).id());

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
