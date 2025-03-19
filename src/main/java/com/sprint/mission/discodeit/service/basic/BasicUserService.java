package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.user.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.UserDeletedEvent;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FilePathContents;
import com.sprint.mission.discodeit.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor        // 사용 시 필수 필드에 private final 필수!
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentService binaryContentService;
    private final ApplicationEventPublisher eventPublisher;

    // 유저 생성
    @Override
    public UUID create(CreateUserRequestDto createUserDto, MultipartFile profileImageFile) throws IOException {

        BinaryContent profile = null;

        if (profileImageFile != null) {
            CreateBinaryContentRequestDto createBinaryContentRequestDto = new CreateBinaryContentRequestDto(profileImageFile, FilePathContents.PROFILEIMAGE_DIR);
            profile = binaryContentService.create(createBinaryContentRequestDto);
        }

        User user = new User(
                createUserDto.email(),
                createUserDto.password(),
                createUserDto.name(),
                createUserDto.nickname(),
                createUserDto.phoneNumber(),
                profile
        );

        validationUser(user);
        userRepository.save(user);;

        return user.getId();
    }


    // 유저 단건 조회
    @Override
    public User find(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
    }

    // 유저 다건 조회
    @Override
    public List<UserDto> findAll() {

        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::toDto)
                .toList();
    }

    // 유저 수정
    @Override
    public void updateUser(UUID id, UpdateUserRequestDto updateUserRequestDto, MultipartFile profileImageFile) throws IOException {

        User updateUser = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        // 업데이트 시 해당 데이터가 null이면 기존 정보를, 아니면 새로운 정보를 저장
        updateUser.updateEmail(updateUserRequestDto.email());
        updateUser.updatePassword(updateUserRequestDto.password());
        updateUser.updateUsername(updateUserRequestDto.name());
        updateUser.updateNickname(updateUserRequestDto.nickname());
        updateUser.updatePhoneNumber(updateUserRequestDto.phoneNumber());

        if (!profileImageFile.isEmpty()) {
            BinaryContent profileImage = new BinaryContent(profileImageFile, FilePathContents.PROFILEIMAGE_DIR);
            updateUser.updateProfile(profileImage);
        }

        userRepository.save(updateUser);
    }

    // 유저 삭제
    @Override
    public void delete(UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        UUID profileImageId = user.getProfile().getId();
        UUID userStatusId = user.getStatus().getId();

        // 유저 삭제 이벤트 발생
        eventPublisher.publishEvent(new UserDeletedEvent(id, profileImageId, userStatusId));

        userRepository.deleteById(id);

    }

    // 유효성 검사
    private void validationUser(User user) {
        Email email = user.getEmail();
        String name = user.getUsername();

        if (checkIsEmailExist(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        } else if (checkIsNameExist(name)) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }
    }

    // 이메일 가입 여부 확인
    private boolean checkIsEmailExist(Email email) {

        String emailString = email.toString();

        // 가입된 이메일일 경우 true 반환
        return userRepository.findAll().stream()
                .map(user -> user.getEmail().toString())
                .anyMatch(emailString::equals);
    }

    // 동일 이름 존재 여부 확인
    private boolean checkIsNameExist(String name) {

        // 가입된 이름일 경우 true 반환
        return userRepository.findAll().stream()
                .map(User::getUsername)
                .anyMatch(name::equals);
    }
}