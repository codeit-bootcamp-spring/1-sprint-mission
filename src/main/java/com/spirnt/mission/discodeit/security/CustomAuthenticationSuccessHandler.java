package com.spirnt.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final SessionRegistry sessionRegistry;
    private final RememberMeServices rememberMeServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        // 인증 정보 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // 세션에도 저장해야 다음 요청에서도 인증 유지됨
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            context);

        // SessionRegistry에 세션 등록하기
        sessionRegistry.registerNewSession(session.getId(), authentication.getPrincipal());

        // remember-me = true라면 쿠키 추가
        rememberMeServices.loginSuccess(request, response, authentication);

        // 인증된 사용자 정보
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // DTO 매핑
        User user = userDetails.getUser();
        UserDto userDto = UserDto.from(user, isUserOnline(user.getUsername()));

        // response
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), userDto);

    }

    // 세션에 username이 있는지 확인하기
    private boolean isUserOnline(String username) {
        return sessionRegistry.getAllPrincipals().stream()
            .filter(principal -> principal instanceof UserDetails)
            .map(principal -> (UserDetails) principal)
            .anyMatch(userDetails -> userDetails.getUsername().equals(username));
    }

}
