package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.controller.swagger.AuthApiDocs;
import com.spirnt.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.security.CustomUserDetails;
import com.spirnt.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

    private final AuthService authService;

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
        return ResponseEntity.ok(csrfToken);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal CustomUserDetails me) {
        UserDto userDto = authService.getMe(me);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 최소 ROLE_ADMIN 권한 필요
    public ResponseEntity<UserDto> updateRole(
        @RequestBody UserRoleUpdateRequest userRoleUpdateRequest) {
        UserDto userDto = authService.updateRole(userRoleUpdateRequest);
        return ResponseEntity.ok(userDto);
    }

}
