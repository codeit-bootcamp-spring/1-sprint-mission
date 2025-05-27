package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.CsrfTokenDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserExceptions;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

    private final UserMapper userMapper;

    private final UserRepository userRepository;
    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfTokenDto> getCsrfToken(HttpServletRequest request) {
        log.info("Starting CSRF token request");

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrfToken == null) {
            log.warn("CSRF token not found in request attributes");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        CsrfTokenDto csrfTokenDto = new CsrfTokenDto(
            csrfToken.getToken(),
            csrfToken.getHeaderName(),
            csrfToken.getParameterName()
        );

        log.info("CSRF token issued successfully");
        return ResponseEntity.ok(csrfTokenDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        log.info("Starting get current user request");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            log.warn("No authenticated user found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDto userDto = userMapper.toDto(userDetails.getUser());

        log.info("Current user retrieved: userId={}, username={}", userDto.id(),
            userDto.username());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/role")
    @Transactional
    public ResponseEntity<UserDto> updateUserRole(@Valid @RequestBody RoleUpdateRequest request) {
        log.info("Processing role update: userId={}, newRole={}", request.userId(),
            request.newRole());

        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> {
                log.warn("User not found for role update: userId={}", request.userId());
                return UserExceptions.notFound(request.userId());
            });

        // 권한 변경
        user.updateRole(request.newRole());
        userRepository.save(user);

        // 해당 사용자의 모든 세션 무효화 (강제 로그아웃)
        invalidateUserSessions(user.getUsername());

        UserDto updatedUser = userMapper.toDto(user);
        log.info("User role updated successfully: userId={}, newRole={}", user.getId(),
            request.newRole());

        return ResponseEntity.ok(updatedUser);
    }

    private void invalidateUserSessions(String username) {
        log.info("Invalidating all sessions for user: {}", username);

        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for (Object principal : allPrincipals) {
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                if (userDetails.getUsername().equals(username)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal,
                        false);
                    for (SessionInformation session : sessions) {
                        log.debug("Expiring session: {}", session.getSessionId());
                        session.expireNow();
                    }
                }
            }
        }
    }

}
