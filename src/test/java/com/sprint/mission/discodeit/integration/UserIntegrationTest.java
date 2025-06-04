package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.error.ErrorResponse;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(classes = DiscodeitApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @PersistenceContext
  private EntityManager em;

  private String getBaseUrl() {
    return "http://localhost:" + port + "/api/users";
  }

  @BeforeAll
  static void setup() throws Exception {
    Properties properties = new Properties();
    properties.load(new FileInputStream(".env"));

    System.setProperty("AWS_REGION", properties.getProperty("AWS_REGION"));
    System.setProperty("AWS_S3_BUCKET", properties.getProperty("AWS_S3_BUCKET"));
    System.setProperty("AWS_S3_PRESIGNED_URL_EXPIRATION",
        properties.getProperty("AWS_S3_PRESIGNED_URL_EXPIRATION"));
  }

  @Test
  @DisplayName("프로필 있는 유저를 생성하고 올바른 응답을 받을 수 있다")
  public void createUser_success() throws Exception {

    CreateUserRequest createRequest = new CreateUserRequest("testuser", "123123",
        "test@example.com");

    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> userPart = new HttpEntity<>(objectMapper.writeValueAsString(createRequest),
        jsonHeaders);
    parts.add("userCreateRequest", userPart);

    byte[] fileContent = "dummy image content".getBytes(StandardCharsets.UTF_8);
    ByteArrayResource fileResource = new ByteArrayResource(fileContent) {
      @Override
      public String getFilename() {
        return "profile.png";
      }
    };
    parts.add("profile", fileResource);

    ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), parts, String.class);
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

    UserDto createdUser = objectMapper.readValue(response.getBody(), UserDto.class);
    assertThat(createdUser.username()).isEqualTo("testuser");
    assertThat(createdUser.email()).isEqualTo("test@example.com");
    assertThat(createdUser.profile().fileName()).isEqualTo("profile.png");
  }


  @Test
  @DisplayName("이미 존재하는 이메일로 가입할 수 없다")
  void createUser_fail_emailDuplication() throws Exception {
    CreateUserRequest createRequest = new CreateUserRequest("testuser", "123123",
        "test@example.com");

    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> userPart = new HttpEntity<>(objectMapper.writeValueAsString(createRequest),
        jsonHeaders);
    parts.add("userCreateRequest", userPart);

    ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), parts, String.class);
    parts.clear();
    jsonHeaders.clear();

    CreateUserRequest createRequest2 = new CreateUserRequest("testuser2", "123123",
        "test@example.com");
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> userPart2 = new HttpEntity<>(objectMapper.writeValueAsString(createRequest2),
        jsonHeaders);

    parts.add("userCreateRequest", userPart2);

    ResponseEntity<String> response2 = restTemplate.postForEntity(getBaseUrl(), parts,
        String.class);
    assertThat(response2.getStatusCode().is4xxClientError()).isTrue();

    ErrorResponse errorResponse = objectMapper.readValue(response2.getBody(), ErrorResponse.class);
    assertThat(errorResponse.getStatus()).isEqualTo(409);
    assertThat(errorResponse.getCode()).isEqualTo("DUPLICATE_ERROR");
  }

  @Test
  @DisplayName("사용자 정보를 업데이트 할 수 있다")
  void updateUser_success() throws Exception {

    // given
    CreateUserRequest createRequest = new CreateUserRequest("testuser", "123123",
        "test@example.com");

    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> userPart = new HttpEntity<>(objectMapper.writeValueAsString(createRequest),
        jsonHeaders);
    parts.add("userCreateRequest", userPart);
    ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), parts, String.class);
    UserDto createdUser = objectMapper.readValue(response.getBody(), UserDto.class);

    assertThat(createdUser.username()).isEqualTo("testuser");

    parts.clear();
    jsonHeaders.clear();

    String userId = createdUser.id().toString();
    UserUpdateDto updateDto = new UserUpdateDto(
        "updateduser",
        createRequest.email(),
        createRequest.password()
    );

    // when
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> updatePart = new HttpEntity<>(objectMapper.writeValueAsString(updateDto),
        jsonHeaders);
    parts.add("userUpdateRequest", updatePart);
    restTemplate.patchForObject(getBaseUrl() + "/" + createdUser.id(), parts, String.class);

    ResponseEntity<UserDto> updatedUser = restTemplate.getForEntity(
        getBaseUrl() + "/" + userId, UserDto.class);
    assertThat(updatedUser.getBody().username()).isEqualTo("updateduser");
    assertThat(userRepository.findAll()).hasSize(1);
  }

  @Test
  @DisplayName("잘못된 userId 입력시 예외를 던진다")
  void updateUser_throwsException() throws Exception {
    // given
    UserUpdateDto updateDto = new UserUpdateDto("tmp", "tmptmp@gmail.com", "tmptmp");
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> userPart = new HttpEntity<>(objectMapper.writeValueAsString(updateDto),
        jsonHeaders);
    parts.add("userUpdateRequest", userPart);

    String randomId = UUID.randomUUID().toString();

    // when

    String result = restTemplate.patchForObject(
        getBaseUrl() + "/" + randomId, parts, String.class
    );

    ErrorResponse errorResponse = objectMapper.readValue(result, ErrorResponse.class);

    //then
    assertThat(errorResponse.getStatus()).isEqualTo(404);
    assertThat(errorResponse.getExceptionType()).isEqualTo("UserNotFoundException");
  }


  @Test
  @DisplayName("존재하는 사용자를 삭제할 수 있다")
  void deleteUser_success() throws Exception {
    // given
    CreateUserRequest createRequest = new CreateUserRequest("deleteTest", "pw123",
        "delete@example.com");
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> userPart = new HttpEntity<>(objectMapper.writeValueAsString(createRequest),
        headers);
    parts.add("userCreateRequest", userPart);

    ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), parts, String.class);
    UserDto createdUser = objectMapper.readValue(response.getBody(), UserDto.class);

    assertThat(userRepository.findAll()).hasSize(1);

    // when
    restTemplate.delete(getBaseUrl() + "/" + createdUser.id());

    //then
    assertThat(userRepository.findAll()).isEmpty();
  }

  @Test
  @DisplayName("잘못된 형식의 UUID로 삭제 시도시 예외를 던진다")
  void deleteUser_fail_whenWrongUUIDFormat() {
    // given
    String invalidUuid = "invalid uuid";

    // when
    ResponseEntity<ErrorResponse> response =
        restTemplate.exchange(
            getBaseUrl() + "/" + invalidUuid,
            HttpMethod.DELETE,
            null,
            ErrorResponse.class
        );
    ErrorResponse errorResponse = response.getBody();

    //then
    assertThat(errorResponse.getStatus()).isEqualTo(400);
    assertThat(errorResponse.getExceptionType()).isEqualTo("MethodArgumentTypeMismatchException");
  }

  @Test
  @DisplayName("사용자 목록을 조회할 수 있다")
  void getUsers_success() throws Exception {
    //given
    CreateUserRequest request1 = new CreateUserRequest("user1", "12341234", "user1@example.com");
    CreateUserRequest request2 = new CreateUserRequest("user2", "12341234", "user2@example.com");

    for (CreateUserRequest req : List.of(request1, request2)) {
      MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> userPart = new HttpEntity<>(objectMapper.writeValueAsString(req), headers);
      parts.add("userCreateRequest", userPart);
      restTemplate.postForEntity(getBaseUrl(), parts, String.class);
    }

    // when
    ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl(), String.class);

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    UserDto[] users = objectMapper.readValue(response.getBody(), UserDto[].class);
    assertThat(users.length).isEqualTo(2);
  }

  @Test
  @DisplayName("사용자가 없을 경우 빈 리스트를 반환한다")
  void getUsers_returnsEmptyList_whenNoUserExists() throws JsonProcessingException {
    // when
    ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl(), String.class);

    // then
    List<?> users = objectMapper.readValue(response.getBody(), List.class);
    assertThat(users).isEmpty();
  }
}
