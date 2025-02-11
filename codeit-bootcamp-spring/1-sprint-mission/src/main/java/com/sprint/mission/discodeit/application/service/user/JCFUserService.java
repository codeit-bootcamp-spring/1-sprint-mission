package com.sprint.mission.discodeit.application.service.user;

import com.sprint.mission.discodeit.application.auth.PasswordEncoder;
import com.sprint.mission.discodeit.application.dto.user.ChangePasswordRequestDto;
import com.sprint.mission.discodeit.application.dto.user.LoginRequestDto;
import com.sprint.mission.discodeit.application.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.application.dto.user.joinUserRequestDto;
import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.application.service.user.converter.UserConverter;
import com.sprint.mission.discodeit.application.service.userstatus.UserStatusService;
import com.sprint.mission.discodeit.domain.channel.Channel;
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
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.repository.channel.interfaces.ChannelRepository;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class JCFUserService implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final UserStatusService userStatusService;
    private final ChannelRepository channelRepository;

    public JCFUserService(
            UserRepository userRepository,
            UserConverter userConverter,
            PasswordEncoder passwordEncoder,
            UserStatusService userStatusService,
            ChannelRepository channelRepository
    ) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
        this.userStatusService = userStatusService;
        this.channelRepository = channelRepository;
    }

    @Override
    public UserResponseDto join(joinUserRequestDto requestDto) {
        throwEmailAlreadyUsed(requestDto.email());
        throwUsernameAlreadyUsed(requestDto.username());
        PasswordValidator.validateOrThrow(requestDto.password());
        User savedUser = userRepository.save(toUserWithPasswordEncode(requestDto));
        UserStatus savedUserStatus = userStatusService.createAtFirstJoin(savedUser);
        return userConverter.toDto(savedUser, savedUserStatus);
    }

    @Override
    public void login(LoginRequestDto requestDto) {
        userRepository.findOneByEmail(new Email(requestDto.email()))
                .filter(user -> matchUserPassword(requestDto.password(), user.getPasswordValue()))
                .orElseThrow(() -> new InvalidLoginException(ErrorCode.INVALID_CREDENTIALS));
        // 3. 로그인 성공 시 로직 구현하기 - 쿠키(어떤 값 담지?), 세부적인건 우선순위를 뒤로 미루기
        // etc. 스프링 시큐리티 적용 시 리팩터링 최대한 변경에 유연하게 코딩
    }

    @Override
    public User findOneByUserIdOrThrow(UUID uuid) {
        return userRepository.findOneById(uuid)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserStatus userStatus = userStatusService.findOneByUser(user);
                    return userConverter.toDto(user, userStatus);
                })
                .toList();
    }

    @Override
    public void changePassword(UUID userId, ChangePasswordRequestDto requestDto) {
        PasswordValidator.validateOrThrow(requestDto.password());
        User foundUser = findOneByUserIdOrThrow(userId);
        foundUser.updatePassword(passwordEncoder.encode(requestDto.password()));
        userRepository.save(foundUser);
    }

    @Override
    public void quitUser(UUID userId) {
        User foundUser = findOneByUserIdOrThrow(userId);
        List<Channel> channels = channelRepository.findAllByUserId(userId);
        channels.forEach(channel -> channel.quitChannel(foundUser));
        userStatusService.delete(foundUser);
        userRepository.deleteByUser(foundUser);
    }

    private void throwEmailAlreadyUsed(String email) {
        if (userRepository.isExistByEmail(new Email(email))) {
            throw new AlreadyUserExistsException(ErrorCode.DUPLICATE_EMAIL, email);
        }
    }

    private void throwUsernameAlreadyUsed(String username) {
        if (userRepository.isExistByUsername(new Username(username))) {
            throw new AlreadyUserExistsException(ErrorCode.DUPLICATE_USERNAME, username);
        }
    }

    private boolean matchUserPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private User toUserWithPasswordEncode(joinUserRequestDto requestDto) {
        return new User(
                new Nickname(requestDto.nickname()),
                new Username(requestDto.username()),
                new Email(requestDto.email()),
                new Password(passwordEncoder.encode(requestDto.password())),
                new BirthDate(requestDto.birthDate()),
                EmailSubscriptionStatus.UNSUBSCRIBED
        );
    }
}
