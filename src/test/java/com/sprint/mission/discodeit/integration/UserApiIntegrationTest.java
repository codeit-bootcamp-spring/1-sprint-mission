package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateResponse;
import com.sprint.mission.discodeit.service.UserService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiIntegrationTest {

  @Autowired
  private UserService userService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("유저 생성")
  void createUserBasic() throws Exception {
    // UserCreateRequest 객체 생성
    UserCreateRequest request = new UserCreateRequest("userC", "userc@mail.com", "password1234");

    // 객체를 JSON 문자열로 변환
    String userCreateRequestJson = objectMapper.writeValueAsString(request);

    // multipart/form-data 준비
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    // JSON 문자열을 HttpEntity로 감싸서 Content-Type을 application/json으로 설정
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonPart = new HttpEntity<>(userCreateRequestJson, jsonHeaders);

    // MultiValueMap에 추가
    body.add("userCreateRequest", jsonPart);

    // 전체 요청에 대한 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    ResponseEntity<UserCreateResponse> response = restTemplate
        .postForEntity("/api/users", requestEntity, UserCreateResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response).isNotNull();
    assertThat("userC").isEqualTo(response.getBody().username());
    assertThat("userc@mail.com").isEqualTo(response.getBody().email());
  }

  @Test
  @DisplayName("유저 생성 - 프로필 이미지 함께")
  void createUserWithProfileImg() throws Exception {
    UserCreateRequest request = new UserCreateRequest("userD", "userd@mail.com", "password1234");

    String requestJson = objectMapper.writeValueAsString(request);
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonEntity = new HttpEntity<>(requestJson, jsonHeaders);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("userCreateRequest", jsonEntity);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    Path path = Paths.get(".discodeit/storage/4d6496bf-d0c8-4df8-9cb2-3aa6a9d82eae");
    byte[] content = Files.readAllBytes(path);

    ByteArrayResource fileResource = new ByteArrayResource(content) {
      @Override
      public String getFilename() {
        return "test";
      }
    };

    HttpHeaders profileHeaders = new HttpHeaders();
    profileHeaders.setContentType(MediaType.IMAGE_JPEG);
    HttpEntity<ByteArrayResource> filePart = new HttpEntity<>(fileResource,
        profileHeaders);

    body.add("profile", filePart);

    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

    ResponseEntity<UserCreateResponse> response = restTemplate
        .postForEntity("/api/users", httpEntity, UserCreateResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat("userD").isEqualTo(response.getBody().username());
    assertThat("userd@mail.com").isEqualTo(response.getBody().email());
    assertThat("test").isEqualTo(response.getBody().profile().fileName());
  }

  @Test
  @DisplayName("유저 수정")
  void modifyUser() throws Exception {
    UserUpdateRequest request = new UserUpdateRequest("newUser3", "new3@mail.com", "newPassword");
    UUID userId = UUID.fromString("5c4e3f2d-8d1b-6a0c-c9e7-f6d4e2c0b9a8");

    String requestJson = objectMapper.writeValueAsString(request);
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonEntity = new HttpEntity<>(requestJson, jsonHeaders);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("userUpdateRequest", jsonEntity);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

    ResponseEntity<UserUpdateResponse> response = restTemplate.exchange(
        "/api/users/{userId}",
        HttpMethod.PATCH,
        httpEntity,
        UserUpdateResponse.class,
        userId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat("newUser3").isEqualTo(response.getBody().username());
    assertThat("new3@mail.com").isEqualTo(response.getBody().email());
  }

  @Test
  @DisplayName("유저 수정 - 프로필 이미지 업데이트")
  void modifyUserWithProfileImg() throws Exception {
    UUID user1Id = UUID.fromString("3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0");
    UserUpdateRequest request = new UserUpdateRequest("newUser", "new@mail.com", "newPassword");

    String requestJson = objectMapper.writeValueAsString(request);
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonEntity = new HttpEntity<>(requestJson, jsonHeaders);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("userUpdateRequest", jsonEntity);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    Path path = Paths.get(".discodeit/storage/4d6496bf-d0c8-4df8-9cb2-3aa6a9d82eae");
    byte[] content = Files.readAllBytes(path);

    ByteArrayResource fileResource = new ByteArrayResource(content) {
      @Override
      public String getFilename() {
        return "test";
      }
    };

    HttpHeaders profileHeaders = new HttpHeaders();
    profileHeaders.setContentType(MediaType.IMAGE_JPEG);
    HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(fileResource,
        profileHeaders);
    body.add("profile", fileEntity);

    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

    ResponseEntity<UserUpdateResponse> response = restTemplate.exchange(
        "/api/users/{userId}",
        HttpMethod.PATCH,
        httpEntity,
        UserUpdateResponse.class,
        user1Id
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response).isNotNull();
    assertThat(response.getBody().profile()).isNotNull();
    assertThat("test").isEqualTo(response.getBody().profile().fileName());
  }


  @Test
  @DisplayName("유저 삭제")
  void deleteUser() throws Exception {
    UUID user1Id = UUID.fromString("3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0");

    ResponseEntity<UUID> response = restTemplate.exchange(
        "/api/users/{userId}",
        HttpMethod.DELETE,
        null,
        UUID.class,
        user1Id
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("유저 삭제 - 없는 유저 삭제")
  void notFoundUserDelete() throws Exception {
    UUID noneUser = UUID.fromString("8f2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0");

    RestClientException exception = Assertions.assertThrows(RestClientException.class,
        () ->
            restTemplate.exchange(
                "/api/users/{userId}",
                HttpMethod.DELETE,
                null,
                UUID.class,
                noneUser
            ));

    assertThat(exception.getClass()).isEqualTo(RestClientException.class);
  }
}
