package com.sprint.mission.discodeit.web.login;

import com.sprint.mission.discodeit.service.LoginService;
import com.sprint.mission.discodeit.web.dto.CheckUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<CheckUserDto> login(@Valid @RequestBody LoginForm loginForm) {
        CheckUserDto login = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if(login == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(login, HttpStatus.OK);
    }
}
