package discodeit.controller;

import discodeit.dto.user.LoginRequest;
import discodeit.entity.User;
import discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        User login = authService.login(request);
        return ResponseEntity.ok("사용자 ID: " + login.getId() + " 해당 사용자가 로그인했습니다!!");
    }

}
