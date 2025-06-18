package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.config.CacheConfig;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.event.RoleChangedEvent;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

    @Value("${discodeit.admin.username}")
    private String username;
    @Value("${discodeit.admin.password}")
    private String password;
    @Value("${discodeit.admin.email}")
    private String email;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;
    private final CacheManager cacheManager;

    @Transactional
    @Override
    public UserDto initAdmin() {
        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
            log.warn("이미 어드민이 존재합니다.");
            return null;
        }

        String encodedPassword = passwordEncoder.encode(password);
        User admin = new User(username, email, encodedPassword, null);
        admin.updateRole(Role.ADMIN);
        userRepository.save(admin);

        UserDto adminDto = userMapper.toDto(admin);

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    evictAllUsersCache();
                    log.info("어드민 초기화 완료: {} - 트랜잭션 커밋 후 전체 사용자 목록 캐시 무효화", adminDto);
                }
            }
        );

        return adminDto;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @CachePut(value = CacheConfig.USER_DETAIL, key = "#request.userId()")
    @Override
    public UserDto updateRole(RoleUpdateRequest request) {
        UUID userId = request.userId();
        User user = userRepository.findById(userId)
            .orElseThrow(() -> UserNotFoundException.withId(userId));

        Role oldRole = user.getRole();
        Role newRole = request.newRole();

        user.updateRole(request.newRole());

        jwtService.invalidateJwtSession(user.getId());

        UserDto updatedUserDto = userMapper.toDto(user);

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    RoleChangedEvent event = new RoleChangedEvent(userId, oldRole,
                        newRole);
                    eventPublisher.publishEvent(event);

                    evictAllUsersCache();

                    log.info(
                        "권한 변경 완료: userId={}, {} -> {} - 트랜잭션 커밋 후 사용자 상세 캐시 갱신, 전체 사용자 목록 캐시 무효화, 권한 변경 이벤트 발행",
                        userId, oldRole, newRole);
                }
            }
        );

        return updatedUserDto;
    }

    private void evictAllUsersCache() {
        try {
            org.springframework.cache.Cache cache = cacheManager.getCache(CacheConfig.ALL_USERS);
            if (cache != null) {
                cache.clear();
                log.debug("전체 사용자 목록 캐시 무효화 완료");
            } else {
                log.warn("전체 사용자 캐시를 찾을 수 없음: cacheName={}", CacheConfig.ALL_USERS);
            }
        } catch (Exception e) {
            log.error("전체 사용자 목록 캐시 무효화 실패", e);
        }
    }
}
