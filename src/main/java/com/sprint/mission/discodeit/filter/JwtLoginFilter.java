package com.sprint.mission.discodeit.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.auth.LoginRequest;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.status.UserSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtLoginFilter extends OncePerRequestFilter {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final JwtSessionRepository jwtSessionRepository;
  private final ObjectMapper objectMapper;
  private final UserSessionService userSessionService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
        LoginRequest.class);

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginRequest.username(), loginRequest.password());

    Authentication authenticate = authenticationManager.authenticate(authenticationToken);

    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authenticate.getPrincipal();

    UserDto dto = getUserDto(userDetails);
    if (jwtSessionRepository.existsByUser_Id(dto.id())) {
      jwtService.invalidTokenByUserId(dto.id());
    }

    String accessToken = jwtService.generateAccessToken(dto);
    String refreshToken = jwtService.generateRefreshToken(dto);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.OK.value());
    response.getWriter().write(objectMapper.writeValueAsString(accessToken));

    Cookie cookie = new Cookie("refresh_token", refreshToken);
    cookie.setHttpOnly(false);
    cookie.setPath("/");
    cookie.setMaxAge((int) jwtService.getRefreshTokenExpiration() / 1000);
    response.addCookie(cookie);

    log.info("로그인 성공:{}", dto.id());
  }

  public UserDto getUserDto(DiscodeitUserDetails userDetails) {
    User user = userRepository.findById(userDetails.getId())
        .orElseThrow(
            () -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, Map.of(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            )));

    BinaryContent profile = user.getProfile();
    BinaryContentDto binaryContentDto;
    if (profile == null) {
      binaryContentDto = null;
    } else {
      binaryContentDto = new BinaryContentDto(user.getId(), profile.getFileName(),
          profile.getSize(),
          profile.getContentType());
    }

    return UserDto.builder()
        .id(userDetails.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(binaryContentDto)
        .online(userSessionService.isOnline(user.getUsername()))
        .Role(user.getRole())
        .build();
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return !("/api/auth/login".equals(request.getServletPath())
        && HttpMethod.POST.matches(request.getMethod()));
  }
}
