package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

//    @PostMapping(value = "/login")
//    public ResponseEntity<UserDto> login(
//        @Valid @RequestBody LoginRequest loginRequest) {
//        return ResponseEntity.status(HttpStatus.OK)
//            .body(authService.login(loginRequest));
//    }

    @GetMapping("/csrf-token")
    public Map<String, String> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // 반환할 때 프론트엔드가 header와 cookie를 구성할 수 있도록 키도 같이 줌
        return Map.of(
            "headerName", csrfToken.getHeaderName(),     // 보통 X-CSRF-TOKEN
            "parameterName", csrfToken.getParameterName(), // form 전송 시 필요
            "token", csrfToken.getToken()
            // *** 여기서 중요한 건 getToken()이 실제 masked token이여야 함 ***
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserInfo(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Unauthorized"); // 임시 예외
        }

        return ResponseEntity.ok(authService.getUserInfo(userDetails));
    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> updateRole(@RequestBody @Valid UserRoleUpdateRequest request) {
        return ResponseEntity.ok(authService.updateUserRole(request));
    }
}
