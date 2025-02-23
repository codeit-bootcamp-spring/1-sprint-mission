package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.user.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.FindUserResponseDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.UserDeletedEvent;
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
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor        // 사용 시 필수 필드에 private final 필수!
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentService binaryContentService;
    private final ApplicationEventPublisher eventPublisher;

    // User 생성(가입)
    @Override
    public UUID create(CreateUserRequestDto createUserDto) throws IOException {

        MultipartFile profileImageFile = createUserDto.profileImageFile();
        UUID profileImageId = null;

        if (profileImageFile != null) {
            CreateBinaryContentRequestDto createBinaryContentRequestDto = new CreateBinaryContentRequestDto(profileImageFile, FilePathContents.PROFILEIMAGE_DIR);
            profileImageId = binaryContentService.create(createBinaryContentRequestDto);
        }

        User user = new User(
                createUserDto.email(),
                createUserDto.password(),
                createUserDto.name(),
                createUserDto.nickname(),
                createUserDto.phoneNumber(),
                profileImageId
        );

        validationUser(user);
        userRepository.save(user);;

        return user.getId();
    }


    // 데이터 읽기
    // id 같은 사람 읽기
    @Override
    public FindUserResponseDto find(UUID id) {

        userIsExist(id);

        User user = userRepository.load().get(id);

        return FindUserResponseDto.fromEntity(user);
    }

    // 모든 데이터 읽어오기
    @Override
    public List<FindUserResponseDto> findAll() {

        List<User> users = changeMapToList(userRepository.load());

        return users.stream()
                .map(FindUserResponseDto::fromEntity)
                .toList();
    }

    // 데이터 수정
    @Override
    public void updateUser(UUID id, UpdateUserRequestDto updateUserRequestDto) throws IOException {
        User updateUser = userRepository.load().get(id);

        // 업데이트 시 해당 데이터가 null이면 기존 정보를, 아니면 새로운 정보를 저장
        updateUser.updateEmail(updateUserRequestDto.email());
        updateUser.updatePassword(updateUserRequestDto.password());
        updateUser.updateName(updateUserRequestDto.name());
        updateUser.updateNickname(updateUserRequestDto.nickname());
        updateUser.updatePhoneNumber(updateUserRequestDto.phoneNumber());

        MultipartFile profileImageFile = updateUserRequestDto.profileImageFile();

        if (!profileImageFile.isEmpty()) {
            BinaryContent profileImage = new BinaryContent(profileImageFile, FilePathContents.PROFILEIMAGE_DIR);
            updateUser.updateProfileImageId(profileImage.getId());
        }

        userRepository.save(updateUser);
    }

    @Override
    public void delete(UUID id) {

        userIsExist(id);

        User user = userRepository.load().get(id);
        UUID profileImageId = user.getProfileImageId();
        UUID userStatusId = user.getUserStatus().getUserId();

        // 유저 삭제 이벤트 발생
        eventPublisher.publishEvent(new UserDeletedEvent(id, profileImageId, userStatusId));

        userRepository.delete(id);

    }

    // 유저 존재 여부 확인
    public void userIsExist(UUID id) {
        Map<UUID, User> users = userRepository.load();

        if (!users.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }

    // 유저 삭제 (회원 탈퇴)
    // 불러온 데이터 List로 변환
    private List<User> changeMapToList(Map<UUID, User> map) {

        return map.values().stream().toList();
    }

    private void validationUser(User user) {
        Email email = user.getEmail();
        String name = user.getName();

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
        return userRepository.load().values().stream()
                .map(user -> user.getEmail().toString())
                .anyMatch(emailString::equals);
    }

    // 동일 이름 존재 여부 확인
    private boolean checkIsNameExist(String name) {

        // 가입된 이름일 경우 true 반환
        return userRepository.load().values().stream()
                .map(User::getName)
                .anyMatch(name::equals);
    }
}