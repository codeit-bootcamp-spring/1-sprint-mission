package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {


  private final UserRepository userRepository;
  //
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  //
  private final UserMapper userMapper;
  //
  private final ApplicationEventPublisher eventPublisher;
  //
  private final ChannelService channelService;
  private final ReadStatusService readStatusService;
  private final ChannelMapper channelMapper;
  private final ChannelRepository channelRepository;


  @Value("${discodeit.admin.username}")
  private String username;
  @Value("${discodeit.admin.password}")
  private String password;
  @Value("${discodeit.admin.email}")
  private String email;

  @Transactional
  public UserDto initAdmin() {
    if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
      log.warn("이미 어드민이 존재합니다.");
      return null;
    }

    String encodedPassword = passwordEncoder.encode(password);
    User admin = new User(username, email, encodedPassword, null);
    admin.updateRole(Role.ADMIN);
    userRepository.save(admin);

    String encodedPassword1 = passwordEncoder.encode("user123123");
    User user1 = new User("user1", "user1@example.com", encodedPassword1, null);
    user1.updateRole(Role.USER);
    user1 = userRepository.save(user1);

    log.info("user1Id={}", user1.getId());

    String encodedPassword2 = passwordEncoder.encode("user234234");
    User user2 = new User("user2", "user2@example.com", encodedPassword2, null);
    user2.updateRole(Role.USER);
    userRepository.save(user2);

    ChannelDto channelDto1 = channelService.createPublicChannel(
        new ChannelPublicRequest("cacheTest1", "@CacheableTest"));
    Channel channel1 = channelRepository.findById(channelDto1.id()).get();
    ChannelDto channelDto2 = channelService.createPublicChannel(
        new ChannelPublicRequest("cacheTest2", "@CacheableTest"));
    Channel channel2 = channelRepository.findById(channelDto2.id()).get();

    readStatusService.createReadStatus(new ReadStatusCreateRequest(user1, channel2, Instant.now()));
    readStatusService.createReadStatus(new ReadStatusCreateRequest(user1, channel1, Instant.now()));

    UserDto adminDto = userMapper.toDto(admin);
    log.info("어드민이 초기화되었습니다. {}", adminDto);
    return adminDto;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  public UserDto changeRole(RoleUpdateRequest request) {
    UUID userId = request.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));
    user.updateRole(request.newRole());

    // 알림 생성
    log.info("알림을 생성합니다. role={}, userId={}", request.newRole(), userId);

    eventPublisher.publishEvent(
        NotificationDto.builder()
            .title("권한 변경")
            .content(request.newRole().toString())
            .type(NotificationType.ROLE_CHANGED)
            .receiverId(userId)
            .targetId(userId)
            .build()
    );

    jwtService.invalidateJwtSession(user.getId());
    return userMapper.toDto(user);
  }
}
