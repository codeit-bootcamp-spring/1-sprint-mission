package com.sprint.mission.discodeit.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.auth.LoginRequest;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.status.UserSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;
  private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
  private final UserSessionService userSessionService;
  private final JwtSessionRepository jwtSessionRepository;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    try {
      LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
          LoginRequest.class);

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          loginRequest.username(), loginRequest.password());

      return getAuthenticationManager().authenticate(authentication);

    } catch (IOException e) {
      throw new BadCredentialsException("IOException 발생");
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {

    removeAlreadySession(authResult);

    saveContext(request, response, authResult);

    User user = getPrincipal(authResult);

    getRememberMeServices().loginSuccess(request, response, authResult);

    UserDto userDto = getUserDto(user);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(response.getWriter(), userDto);
  }

  private void removeAlreadySession(Authentication authResult) {
    sessionRepository.findByIndexNameAndIndexValue(
            FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, authResult.getName()).keySet()
        .forEach(session -> sessionRepository.deleteById(session));
  }


  private UserDto getUserDto(User user) {

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
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .online(userSessionService.isOnline(user.getUsername())) // TODO: 세션으로 바꾼 후 수정해야함
        .profile(binaryContentDto)
        .Role(user.getRole())
        .build();
  }

  private User getPrincipal(Authentication authResult) {
    UserDetails principal = (UserDetails) authResult.getPrincipal();
    return userRepository.findUserByUsername(principal.getUsername());
  }

  private void saveContext(HttpServletRequest request, HttpServletResponse response,
      Authentication authResult) {
    SecurityContextHolder.getContext().setAuthentication(authResult);
    HttpSessionSecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
    contextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
  }
}
