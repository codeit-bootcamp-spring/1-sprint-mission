package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.async.BinaryContentUploadExecutor;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.*;

import com.sprint.mission.discodeit.exception.user.UserDuplicateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.notification.NotificationEvent;
import com.sprint.mission.discodeit.notification.NotificationEventPublisher;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtSessionRepository jwtSessionRepository;
  private final BinaryContentUploadExecutor uploadExecutor;

  private final NotificationEventPublisher notificationEventPublisher;


  @CacheEvict(cacheNames = "allUsers", allEntries = true)
  @Override
  @Transactional
  public UserDto create(UserCreateDTO dto,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new UserDuplicateException("email", dto.getEmail());
    }
    if (userRepository.existsByUsername(dto.getUsername())) {
      throw new UserDuplicateException("username", dto.getUsername());
    }

    BinaryContent nullableProfile = saveBinaryFile(optionalProfileCreateRequest);

    String encodePwd = passwordEncoder.encode(dto.getPassword());

    User user = new User(dto.getUsername(), dto.getEmail(), encodePwd, nullableProfile, Role.USER);

    //cascade persist
    User saveUser = userRepository.save(user);

    log.info("사용자 생성 완료 id: {}", saveUser.getId());

    return userMapper.toDto(saveUser, isUserOnline(saveUser));
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto find(UUID id) {
    User findUser = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
    return userMapper.toDto(findUser, isUserOnline(findUser));
  }


  @Cacheable(cacheNames = "allUsers")
  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(user -> userMapper.toDto(user, isUserOnline(user)))
        .toList();
  }

  @CacheEvict(cacheNames = "allUsers", allEntries = true)
  @Override
  @Transactional
  public UserDto update(UUID id, UserUpdateDTO dto,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User findUser = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

    if (userRepository.existsByEmail(dto.getNewEmail())) {
      throw new UserDuplicateException("email", dto.getNewEmail());
    }
    if (userRepository.existsByUsername(dto.getNewUsername())) {
      throw new UserDuplicateException("username", dto.getNewUsername());
    }

    BinaryContent nullableProfile = saveBinaryFile(optionalProfileCreateRequest);

    findUser.updateUser(dto.getNewUsername(), dto.getNewEmail(), dto.getNewPassword(),
        nullableProfile);

    log.info("사용자 수정 완료 id: {}", findUser.getId());

    return userMapper.toDto(findUser, isUserOnline(findUser));
  }

  @CacheEvict(cacheNames = "allUsers", allEntries = true)
  @Override
  @Transactional
  public void delete(UUID id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }
    userRepository.deleteById(id);
    log.info("사용자 삭제 완료 id: {}", id);
  }

/*  @Transactional
  public BinaryContent saveBinaryFile(
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    return optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.getFileName();
          String contentType = profileRequest.getContentType();
          byte[] bytes = profileRequest.getBytes();

          BinaryContent binaryContent = binaryContentRepository.save(
              new BinaryContent(fileName, contentType, (long) bytes.length));

          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);
  }*/

  @Transactional
  public BinaryContent saveBinaryFile(
          Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

    return optionalProfileCreateRequest
            .map(profileRequest -> {
              String fileName = profileRequest.getFileName();
              String contentType = profileRequest.getContentType();
              byte[] bytes = profileRequest.getBytes();

              BinaryContent binaryContent = new BinaryContent(fileName, contentType, (long) bytes.length);
              binaryContent.updateUploadStatus(BinaryContentUploadStatus.WAITING);
              BinaryContent saved = binaryContentRepository.save(binaryContent);

              //비동기 업로드 등록 -> 트랜잭션 커밋 이후 실행
              TransactionSynchronizationManager.registerSynchronization(
                      new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                          String requestId = MDC.get("requestId"); //null 방지 필요 시 대체 처리
                          uploadExecutor.uploadAsync(saved.getId(), bytes, requestId);
                        }
                      }
              );

              return saved;
            })
            .orElse(null);
  }



  @Transactional
  public UserDto updateRole(UUID userId, Role newRole) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
    user.updateRole(newRole);

    //알림 이벤트 발행 (트랜잭션 안에서 호출 → AFTER_COMMIT 리스너에서 처리됨)
    notificationEventPublisher.publish(new NotificationEvent(
            userId,
            "권한이 변경되었어요",
            "새로운 권한: " + newRole.name(),
            NotificationType.ROLE_CHANGED,
            userId
    ));


    // Jwt 기반 강제 로그아웃 처리
    jwtSessionRepository.findAllByUserId(userId).forEach(JwtSession::revoke);

    return userMapper.toDto(user, isUserOnline(user));
  }

  public boolean isUserOnline(User user) {
    return jwtSessionRepository.existsByUserIdAndRevokedFalse(user.getId());
  }

}