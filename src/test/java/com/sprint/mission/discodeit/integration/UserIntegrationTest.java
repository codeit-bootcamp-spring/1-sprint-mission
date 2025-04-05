package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
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


@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private final String BASE_URL = "/api/users";

  @DisplayName("프로필 없이 사용자 생성을 할 수 있다.")
  @Test
  void createUser_withoutProfile() throws Exception {
    // given
    UserCreateDTO requestDto = new UserCreateDTO("testuser", "test@email.com", "password123");
    String json = objectMapper.writeValueAsString(requestDto);

    // JSON DTO -> Multipart 파트 구성
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonPart = new HttpEntity<>(json, jsonHeaders);

    // multipart/form-data 전체 구성
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("userCreateRequest", jsonPart);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    // when
    ResponseEntity<UserDto> response = restTemplate.postForEntity(
        BASE_URL,
        requestEntity,
        UserDto.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getUsername()).isEqualTo("testuser");
    assertThat(response.getBody().getEmail()).isEqualTo("test@email.com");
    assertThat(response.getBody().getProfile()).isNull(); // 프로필 없었으니까 null
  }

  @DisplayName("사용자 수정을 할 수 있다.")
  @Test
  void updateUser() throws Exception {
    // given
    UUID userId = createUserAndGetId("origin", "origin@email.com");

    UserUpdateDTO updateDto = new UserUpdateDTO("updated", "updated@email.com", "newPassword!");
    String updateJson = objectMapper.writeValueAsString(updateDto);

    HttpHeaders partHeaders = new HttpHeaders();
    partHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonPart = new HttpEntity<>(updateJson, partHeaders);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("userUpdateDTO", jsonPart);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

    // when
    ResponseEntity<UserDto> response = restTemplate.exchange(
        BASE_URL + "/" + userId,
        HttpMethod.PATCH,
        request,
        UserDto.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getUsername()).isEqualTo("updated");
    assertThat(response.getBody().getEmail()).isEqualTo("updated@email.com");
  }

  @DisplayName("사용자 삭제를 할 수 있다.")
  @Test
  void deleteUser() throws Exception {
    // given
    UUID userId = createUserAndGetId("todelete", "delete@email.com");

    // when
    ResponseEntity<Void> response = restTemplate.exchange(
        BASE_URL + "/" + userId,
        HttpMethod.DELETE,
        null,
        Void.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    // 삭제 확인
    ResponseEntity<UserDto[]> getResponse = restTemplate.getForEntity(BASE_URL, UserDto[].class);
    assertThat(getResponse.getBody()).noneMatch(user -> user.getId().equals(userId));
  }

  @Test
  @DisplayName("사용자 목록 조회")
  void getAllUsers() throws Exception {
    // given
    createUserAndGetId("user1", "user1@email.com");
    createUserAndGetId("user2", "user2@email.com");

    // when
    ResponseEntity<UserDto[]> response = restTemplate.getForEntity(BASE_URL, UserDto[].class);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSizeGreaterThanOrEqualTo(2);
    assertThat(response.getBody())
        .extracting(UserDto::getUsername)
        .contains("user1", "user2");
  }


  private UUID createUserAndGetId(String username, String email) throws Exception {
    UserCreateDTO createDto = new UserCreateDTO(username, email, "password123");
    String json = objectMapper.writeValueAsString(createDto);

    HttpHeaders partHeaders = new HttpHeaders();
    partHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonPart = new HttpEntity<>(json, partHeaders);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("userCreateRequest", jsonPart);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

    ResponseEntity<UserDto> response = restTemplate.postForEntity(BASE_URL, request,
        UserDto.class);
    return response.getBody().getId();
  }
}
