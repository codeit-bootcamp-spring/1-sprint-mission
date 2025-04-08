package com.sprint.mission.discodeit.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;

  @Test
  void testCreateUser() {
    // 1) 유저 생성
    UserCreateRequest request = new UserCreateRequest("test", "test@gmail.com", "qwer1234!");
    MultipartFile profile = null;
    MultiValueMap<String, Object> createBody = new LinkedMultiValueMap<>();
    createBody.add("userCreateRequest", request);
    createBody.add("profile", profile);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(createBody, headers);
    ResponseEntity<UserDto> response = restTemplate
        .exchange("/api/users", HttpMethod.POST, entity, UserDto.class);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    // 2) 로그인
    LoginRequest loginRequest = new LoginRequest("test", "qwer1234!");
    ResponseEntity<UserDto> loginResponse = restTemplate
        .postForEntity("/api/auth/login", loginRequest, UserDto.class);
    assertNotNull(loginResponse.getBody());
    assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

    // 3) 유저 수정
    UUID userId = loginResponse.getBody().id();
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest("updateName", "update@gmali.com",
        "qwer12345!");
    MultiValueMap<String, Object> updateBody = new LinkedMultiValueMap<>();
    updateBody.add("userUpdateRequest", userUpdateRequest);
    updateBody.add("profile", profile);
    HttpHeaders updateHeaders = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> updateEntity = new HttpEntity<>(updateBody,
        updateHeaders);
    ResponseEntity<UserDto> updateResponse = restTemplate
        .exchange("/api/users/{userId}", HttpMethod.PATCH, updateEntity, UserDto.class, userId);
    assertNotNull(updateResponse.getBody());
    assertEquals(HttpStatus.OK, updateResponse.getStatusCode());

    // 4) 유저 조회
    ResponseEntity<List<UserDto>> findResponse = restTemplate.exchange(
        "/api/users",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<UserDto>>() {
        }
    );
    assertNotNull(findResponse);
    assertEquals(findResponse.getStatusCode(), HttpStatus.OK);
    assertEquals(findResponse.getBody().get(0).id(), userId);

    // 5) 유저 상태 변경
    UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(Instant.now());
    HttpEntity<UserStatusUpdateRequest> updateRequestHttpEntity = new HttpEntity<>(
        userStatusUpdateRequest);
    ResponseEntity<UserStatusDto> statusResponse = restTemplate.exchange(
        "/api/users/{userId}/userStatus",
        HttpMethod.PATCH,
        updateRequestHttpEntity,
        UserStatusDto.class,
        userId
    );
    assertNotNull(statusResponse);
    assertEquals(statusResponse.getBody().userId(), userId);
    assertEquals(statusResponse.getStatusCode(), HttpStatus.OK);
  }
}
