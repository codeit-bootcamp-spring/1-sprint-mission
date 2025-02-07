package com.sprint.mission.discodeit.application.service.user;

import com.sprint.mission.discodeit.application.auth.PasswordEncoder;
import com.sprint.mission.discodeit.application.dto.JoinUserReqeustDto;
import com.sprint.mission.discodeit.application.dto.LoginRequestDto;
import com.sprint.mission.discodeit.application.dto.UserResponseDto;
import com.sprint.mission.discodeit.application.service.user.converter.UserConverter;
import com.sprint.mission.discodeit.domain.user.BirthDate;
import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.Nickname;
import com.sprint.mission.discodeit.domain.user.Password;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.Username;
import com.sprint.mission.discodeit.domain.user.enums.EmailSubscriptionStatus;
import com.sprint.mission.discodeit.domain.user.exception.AlreadyUserExistsException;
import com.sprint.mission.discodeit.domain.user.exception.InvalidLoginException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.validation.PasswordValidator;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            UserConverter userConverter,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto join(JoinUserReqeustDto requestDto) {
        checkEmailAlreadyUsedOrThrow(requestDto.email());
        checkUsernameAlreadyUsedOrThrow(requestDto.username());
        PasswordValidator.validateOrThrow(requestDto.password());
        User savedUser = userRepository.save(toUserWithPasswordEncode(requestDto));
        return userConverter.toDto(savedUser);
    }

    public void login(LoginRequestDto requestDto) {
        userRepository.findOneByEmail(new Email(requestDto.email()))
                .filter(user -> matchUserPassword(requestDto.password(), user.getPasswordValue()))
                .orElseThrow(() -> new InvalidLoginException(ErrorCode.INVALID_CREDENTIALS));
        // 3. 로그인 성공 시 로직 구현하기 - 쿠키(어떤 값 담지?), 세부적인건 우선순위를 뒤로 미루기
        // etc. 스프링 시큐리티 적용 시 리팩터링 최대한 변경에 유연하게 코딩
    }

    public User findOneByIdOrThrow(UUID uuid) {
        return userRepository.findOneById(uuid)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_FOUND));
    }

    private void checkEmailAlreadyUsedOrThrow(String email) {
        if (userRepository.isExistByEmail(new Email(email))) {
            throw new AlreadyUserExistsException(ErrorCode.DUPLICATE_EMAIL, email);
        }
    }

    private void checkUsernameAlreadyUsedOrThrow(String username) {
        if (userRepository.isExistByUsername(new Username(username))) {
            throw new AlreadyUserExistsException(ErrorCode.DUPLICATE_USERNAME, username);
        }
    }

    private boolean matchUserPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private User toUserWithPasswordEncode(JoinUserReqeustDto requestDto) {
        return User.builder()
                .username(new Username(requestDto.username()))
                .nickname(new Nickname(requestDto.nickname()))
                .email(new Email(requestDto.email()))
                .password(new Password(passwordEncoder.encode(requestDto.password())))
                .birthDate(new BirthDate(requestDto.birthDate()))
                .emailSubscriptionStatus(EmailSubscriptionStatus.UNSUBSCRIBED)
                .build();
    }
}
