package com.spirnt.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.dto.ErrorResponse;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @Transactional
  @DisplayName("사용자 목록 조회 테스트 - 성공")
  void testFindUsersSuccess() {
    ResponseEntity<UserDto[]> response = restTemplate.getForEntity(
        "/api/users",
        UserDto[].class
    );

    System.out.println(response.getBody());

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().length);

  }

  @Test
  @Transactional
  @DisplayName("사용자 생성 테스트 - 성공")
  void testCreateUserSuccess() throws Exception {
    // 컨트롤러에 MultipartFile로 전달
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    String json = new ObjectMapper().writeValueAsString(
        new UserCreateRequest("andy", "andy@mail.com", "password")
    );
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonPart = new HttpEntity<>(json, jsonHeaders);

    body.add("userCreateRequest", jsonPart);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    ResponseEntity<UserDto> response = restTemplate.postForEntity("/api/users", requestEntity,
        UserDto.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody().getId());
    assertEquals(response.getBody().getUsername(), "andy");
    assertEquals(response.getBody().getEmail(), "andy@mail.com");
  }

  @Test
  @Transactional
  @DisplayName("사용자 생성 테스트 - 실패")
  void testCreateUserFail() throws Exception {
    // 컨트롤러에 MultipartFile로 전달
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    String json = new ObjectMapper().writeValueAsString(
        // 이미 data.sql로 저장된 유저 저장 시도
        new UserCreateRequest("alice", "alice@mail.com", "password")
    );
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonPart = new HttpEntity<>(json, jsonHeaders);

    body.add("userCreateRequest", jsonPart);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/api/users", requestEntity,
        ErrorResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("UserAlreadyExistException", response.getBody().getExceptionType());
  }

  @Test
  @Transactional
  @DisplayName("사용자 수정 테스트 - 성공")
  void testUpdateUserSuccess() throws Exception {
    // given
    UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000102");  // data.sql로 저장한 아이디
    UserUpdateRequest updateRequest = new UserUpdateRequest("updatedName", "updated@mail.com",
        "newPassword");

    String json = new ObjectMapper().writeValueAsString(updateRequest);
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonPart = new HttpEntity<>(json, jsonHeaders);
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("userUpdateRequest", jsonPart);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    // when
    ResponseEntity<UserDto> response = restTemplate.exchange(
        "/api/users/" + userId, HttpMethod.PATCH, requestEntity, UserDto.class
    );

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("updatedName", response.getBody().getUsername());
    assertEquals("updated@mail.com", response.getBody().getEmail());
  }

  @Test
  @Transactional
  @DisplayName("사용자 수정 테스트 - 실패")
  void testUpdateUserFail() throws Exception {
    // given
    UUID userId = UUID.randomUUID();  // db에 없는 아이디
    UserUpdateRequest updateRequest = new UserUpdateRequest("updatedName", "updated@mail.com",
        "newPassword");

    String json = new ObjectMapper().writeValueAsString(updateRequest);
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonPart = new HttpEntity<>(json, jsonHeaders);
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("userUpdateRequest", jsonPart);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    // when
    ResponseEntity<ErrorResponse> response = restTemplate.exchange(
        "/api/users/" + userId, HttpMethod.PATCH, requestEntity, ErrorResponse.class
    );

    // then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("UserNotFoundException", response.getBody().getExceptionType());
  }

  @Test
  @Transactional
  @DisplayName("사용자 삭제 테스트 - 성공")
  void testDeleteUserSuccess() {
    // given
    UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000101");  // import.sql로 저장한 아이디

    ResponseEntity<Void> response = restTemplate.exchange(
        "/api/users/" + userId,
        HttpMethod.DELETE,
        null,
        Void.class
    );
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  @Transactional
  @DisplayName("사용자 삭제 테스트 - 실패")
  void testDeleteUserFail() {
    // given
    UUID userId = UUID.randomUUID();
    ResponseEntity<Void> response = restTemplate.exchange(
        "/api/users/" + userId,
        HttpMethod.DELETE,
        null,
        Void.class
    );
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }


}
