package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        // LoginRequest 객체를 생성하여 authenticate 메소드에 전달
        LoginRequest loginRequest = new LoginRequest(username, password);

        try {
            User user = authService.authenticate(loginRequest);
            request.getSession().invalidate();
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedUser", user.getUsername());
            return ResponseEntity.ok("로그인 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " + e.getMessage());
        }
    }

    @PostMapping( "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "로그아웃 성공";
    }

}
