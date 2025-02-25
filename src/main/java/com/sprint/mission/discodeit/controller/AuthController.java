package com.sprint.mission.discodeit.controller;

import ch.qos.logback.core.testUtil.StringListAppender;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginRequest loginRequest,
                        BindingResult bindingResult,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            UserDTO userDTO = userService.find(loginRequest.getEmail());

            if (userDTO != null && userDTO.getPassword().equals(loginRequest.getPassword())) {
                session.setAttribute("userId", userDTO.getId());
                userService.updateOnlineStatus(userDTO.getId(), true);
                return "redirect:/";
            } else {
                log.error("이메일 또는 비밀번호를 확인해주세요.");
                return "login";
            }
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생");
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            userService.updateOnlineStatus(userId, false);
            session.invalidate();
        }
        return "redirect:/auth/login";
    }
}