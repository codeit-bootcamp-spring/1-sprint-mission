package discodeit.service.basic;

import discodeit.dto.binarycontent.AddBinaryContentRequest;
import discodeit.dto.user.CreateUserRequest;
import discodeit.dto.user.UpdatePasswordRequest;
import discodeit.dto.user.UpdatePhoneNumRequest;
import discodeit.dto.user.UpdateProfileRequest;
import discodeit.entity.BinaryContent;
import discodeit.entity.BinaryContentType;
import discodeit.entity.User;
import discodeit.entity.UserStatus;
import discodeit.repository.BinaryContentRepository;
import discodeit.repository.UserStatusRepository;
import discodeit.service.UserService;
import discodeit.repository.UserRepository;
import discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepo;
    private final UserStatusRepository userStatusRepo;
    private final BinaryContentRepository binaryContentRepo;
    private final UserStatusService userStatusService;

    @Override
    public User create(CreateUserRequest createUserRequest, Optional<AddBinaryContentRequest> addBinaryContentRequest) {
        if (isUsernameDuplicate(createUserRequest.username())) {
            throw new IllegalArgumentException("[error] 이미 존재하는 사용자 이름입니다.");
        }
        if (isEmailDuplicate(createUserRequest.email())) {
            throw new IllegalArgumentException("[error] 이미 존재하는 E-mail입니다.");
        }
        if (isValidEmail(createUserRequest.email())) {
            throw new IllegalArgumentException("[error] 유효하지 않은 E-mail 형식입니다.");
        }
        if (isPhoneNumDuplicate(createUserRequest.phoneNum())) {
            throw new IllegalArgumentException("[error] 이미 존재하는 전화번호입니다.");
        }
        if (isValidPhoneNum(createUserRequest.phoneNum())) {
            throw new IllegalArgumentException("[error] 유효하지 않은 전화번호 형식입니다.");
        }

        UUID nullableProfileId = addBinaryContentRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.filename();
                    BinaryContentType contentType = profileRequest.type();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, contentType, (long)bytes.length, bytes);
                    return binaryContentRepo.save(binaryContent).getId();
                })
                .orElse(null);

        User newUser = new User(
                createUserRequest.username(),
                createUserRequest.email(),
                createUserRequest.phoneNum(),
                createUserRequest.password(),
                nullableProfileId);

        UserStatus userStatus = new UserStatus(newUser.getId(), Instant.now());
        userStatusRepo.save(userStatus);
        return userRepo.save(newUser);
    }

    @Override
    public User find(UUID userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 User ID입니다."));
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll()
                .stream()
                .toList();
    }

    @Override
    public User updatePhoneNum(UpdatePhoneNumRequest request) {
        User user = userRepo.findById(request.userId())
                .orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 User ID입니다."));

        if (isPhoneNumDuplicate(request.newPhoneNum())) {
            throw new IllegalArgumentException("[error] 이미 존재하는 전화번호입니다.");
        }
        if (isValidPhoneNum(request.newPhoneNum())) {
            throw new IllegalArgumentException("[error] 유효하지 않은 전화번호 형식입니다. '010-0000-0000' 형식으로 작성해 주세요.");
        }

        user.updatePhoneNum(request.newPhoneNum());
        UserStatus userStatus = userStatusService.findByUserId(user.getId());
        userStatus.update(Instant.now());
        userStatusRepo.save(userStatus);
        return userRepo.save(user);
    }

    @Override
    public User updatePassword(UpdatePasswordRequest request) {
        User user = userRepo.findById(request.userId())
                .orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 User ID입니다."));

        user.updatePassword(request.oldPassword(), request.newPassword());
        return userRepo.save(user);
    }

    @Override
    public User updateProfile(UpdateProfileRequest request) {
        User user = userRepo.findById(request.userId())
                .orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 User ID입니다."));

        user.updateProfile(request.newProfileId());
        UserStatus userStatus = userStatusService.findByUserId(user.getId());
        userStatus.update(Instant.now());
        userStatusRepo.save(userStatus);
        return userRepo.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 User ID입니다."));

        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepo::deleteById);
        userStatusRepo.deleteByUserId(userId);
        userRepo.deleteById(userId);
        System.out.println("[삭제 완료]");
    }


    private boolean isUsernameDuplicate(String username) {
        return userRepo.findAll().stream().anyMatch(user -> user.getUsername().equals(username));
    }
    private boolean isEmailDuplicate(String email) {
        return userRepo.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }
    private boolean isValidEmail(String email) {
        String emailRegExp = "\\w+@\\w+\\.\\w+(\\.\\w+)?";
        return !email.matches(emailRegExp);
    }
    private boolean isPhoneNumDuplicate(String phoneNum) {
        return userRepo.findAll().stream().anyMatch(user -> user.getPhoneNum().equals(phoneNum));
    }
    private boolean isValidPhoneNum(String phoneNum) {
        String phoneNumRegExp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
        return !phoneNum.matches(phoneNumRegExp);
    }

}