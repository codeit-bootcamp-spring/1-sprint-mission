package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service // 서비스 Bean
@RequiredArgsConstructor // 생성자
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User create(UserCreateDto userCreateDto, Optional<BinaryContentCreateDto> profileDto) {
        //username,email 중복 확인
        validationUsernameAndEmail(userCreateDto.username(),userCreateDto.email());

        //프로필 이미지 Id 체크
        UUID profileId = profileIdCheck(profileDto);

        //User 생성
        User user = new User(
                userCreateDto.username(),
                userCreateDto.email(),
                userCreateDto.password(),
                userCreateDto.phoneNumber(),
                userCreateDto.address(),
                profileId);
        User createdUser = userRepository.save(user);

        //UserStatus 생성
        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(createdUser.getId(),now);
        return createdUser;
    }


    @Override
    public UserDto findById(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> toDto(user))
                .orElseThrow(()->new NoSuchElementException("User not found"));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user->toDto(user))
                .toList();
    }

    @Override
    public User update(UUID userId, UserUpdateDto userUpdateDto,Optional<BinaryContentCreateDto> profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("UserId not found"));
        validationUsernameAndEmail(userUpdateDto.newUsername(),userUpdateDto.newEmail());
        UUID profileId = profileIdCheck(profileDto);

        user.update(
                userUpdateDto.newUsername(),
                userUpdateDto.newEmail(),
                userUpdateDto.newPassword(),
                userUpdateDto.newPhonenumber(),
                userUpdateDto.newAddress(),
                profileId
        );

        return userRepository.save(user);
    }

    @Override
    public void deleteById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new NoSuchElementException("userId not found"));
        //관련된 프로필 이미지 삭제
        if(user.getProfileId() != null){
            binaryContentRepository.deleteById(user.getProfileId());
        }
        //UserStatus 삭제
        userStatusRepository.deleteByUserId(userId);
        //유저 삭제
        userRepository.deleteById(user.getId());
    }

    //프로필 이미지 Id 체크
    public UUID profileIdCheck(Optional<BinaryContentCreateDto> profileDto) {
        UUID profileId = profileDto.map(p->{
            String fileName = p.fileName();
            String contentType = p.contentType();
            byte[] bytes = p.bytes();
            BinaryContent binaryContent = new BinaryContent(fileName,(long)bytes.length,contentType,bytes);
            return binaryContentRepository.save(binaryContent).getId();
        }).orElse(null);
        return profileId;
    }
    //username, email 다른 유저와 같은지 체크
    public void validationUsernameAndEmail(String username, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User with username " + username + " already exists");
        }
    }
    //entity -> dto
    public UserDto toDto(User user){
        Boolean online = userStatusRepository.findById(user.getId())
                .map(userStatus->userStatus.isOnline())
                .orElse(null);
        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getProfileId(),
                online
        );
    }
}
