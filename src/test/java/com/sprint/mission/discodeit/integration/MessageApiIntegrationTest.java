package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.entity.message.MessageContentUpdateRequest;
import com.sprint.mission.discodeit.entity.message.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.message.MessageCreateResponse;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Transactional
@Rollback
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageApiIntegrationTest {

  @Autowired
  private MessageService messageService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Value("${file.dir}")
  private String fileDir;

  @Value("${discodeit.storage.local.root-path:.discodeit/storage}")
  private String storagePath;

  // 테스트 실행 전 필요한 디렉토리와 파일 생성
  @BeforeAll
  public static void setUp() throws IOException {
    // 테스트 필요 디렉토리들 생성
    String tmpDir = System.getProperty("java.io.tmpdir");
    File uploadDir = new File(tmpDir + "/discodeit-test/uploads/");
    if (!uploadDir.exists()) {
      uploadDir.mkdirs();
    }

    File storageDir = new File(".discodeit/storage");
    if (!storageDir.exists()) {
      storageDir.mkdirs();
    }

    // 테스트용 더미 이미지 파일 생성 (CI 환경에서도 사용 가능)
    createDummyImageFile(".discodeit/storage/test-message-image.jpg");
  }

  // 테스트용 더미 이미지 파일 생성 메소드
  private static void createDummyImageFile(String path) throws IOException {
    File file = new File(path);
    if (!file.exists()) {
      file.getParentFile().mkdirs();
      // 간단한 더미 이미지 데이터 생성
      byte[] dummyImageContent = new byte[100];
      for (int i = 0; i < dummyImageContent.length; i++) {
        dummyImageContent[i] = (byte) i;
      }
      Files.write(file.toPath(), dummyImageContent);
    }
  }

  @Test
  @DisplayName("메세지 생성")
  @Rollback
  void createMessage() throws Exception {
    UUID channelId = UUID.fromString("2c17a8d1-77d7-437d-811e-d98db3bd30bc");
    UUID userId = UUID.fromString("3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0");
    MessageCreateRequest request = new MessageCreateRequest("content", channelId, userId);
    HttpEntity<String> jsonEntity = getJsonEntity(request);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("messageCreateRequest", jsonEntity);

    HttpHeaders headers = getHeaders();

    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

    ResponseEntity<MessageCreateResponse> response = restTemplate.postForEntity(
        "/api/messages", httpEntity, MessageCreateResponse.class);

    System.out.println("response.getBody() = " + response.getBody());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response).isNotNull();
    assertThat("content").isEqualTo(response.getBody().content());
  }

  @Test
  @Rollback
  @DisplayName("메세지 생성 - 파일 첨부")
  void createMessageWithFile() throws Exception {
    UUID channelId = UUID.fromString("2c17a8d1-77d7-437d-811e-d98db3bd30bc");
    UUID userId = UUID.fromString("3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0");
    MessageCreateRequest request = new MessageCreateRequest("content", channelId, userId);

    HttpEntity<String> jsonEntity = getJsonEntity(request);
    ByteArrayResource file = getTestFile();
    HttpEntity<ByteArrayResource> fileEntity = getFileEntity(file);
    HttpHeaders headers = getHeaders();

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("messageCreateRequest", jsonEntity);
    body.add("attachments", fileEntity);

    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
    ResponseEntity<MessageCreateResponse> response = restTemplate
        .postForEntity("/api/messages", httpEntity, MessageCreateResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response).isNotNull();
    assertThat("content").isEqualTo(response.getBody().content());
    assertThat(response.getBody().attachments()).isNotNull();
    assertThat(1).isEqualTo(response.getBody().attachments().size());
  }


  @Test
  @DisplayName("메세지 생성 실패 - 채널 안에 존재하지 않은 유저가 메세지 보냄")
  void createMessageFail() throws Exception {
    UUID channelId = UUID.fromString("6f17a8d1-77d7-437d-811e-d98db3bd30bc");
    UUID userId = UUID.fromString("002c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0");
    MessageCreateRequest request = new MessageCreateRequest("content", channelId, userId);

    HttpEntity<String> jsonEntity = getJsonEntity(request);
    ByteArrayResource file = getTestFile();
    HttpEntity<ByteArrayResource> fileEntity = getFileEntity(file);
    HttpHeaders headers = getHeaders();

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("messageCreateRequest", jsonEntity);
    body.add("attachments", fileEntity);

    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
    ResponseEntity<MessageCreateResponse> response = restTemplate
        .postForEntity("/api/messages", httpEntity, MessageCreateResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @Rollback
  @DisplayName("메세지 수정")
  void updateMessage() throws Exception {
    UUID messageId = UUID.fromString("40658af2-7bf8-4b1a-ac79-32f417ce391c");
    MessageContentUpdateRequest request = new MessageContentUpdateRequest("newContent");

    String requestJson = objectMapper.writeValueAsString(request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

    ResponseEntity<MessageDto> response = restTemplate.exchange(
        "/api/messages/{messageId}",
        HttpMethod.PATCH,
        httpEntity,
        MessageDto.class,
        messageId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat("newContent").isEqualTo(response.getBody().content());
  }

  @Test
  @DisplayName("메세지 수정 실패 - 없는 메세지 수정 시도")
  void updateMessageNotFound() throws Exception {
    UUID noneMessage = UUID.fromString("99358af2-7bf8-4b1a-ac79-32f417ce391c");
    MessageContentUpdateRequest request = new MessageContentUpdateRequest("newContent");

    String requestJson = objectMapper.writeValueAsString(request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

    ResponseEntity<MessageDto> response = restTemplate.exchange(
        "/api/messages/{messageId}",
        HttpMethod.PATCH,
        httpEntity,
        MessageDto.class,
        noneMessage
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @Rollback
  @DisplayName("메세지 삭제")
  void deleteMessage() throws Exception {
    UUID messageId = UUID.fromString("40658af2-7bf8-4b1a-ac79-32f417ce391c");

    ResponseEntity<String> response = restTemplate.exchange(
        "/api/messages/{messageId}",
        HttpMethod.DELETE,
        null,
        String.class,
        messageId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  private HttpEntity<String> getJsonEntity(MessageCreateRequest request)
      throws JsonProcessingException {
    String requestJson = objectMapper.writeValueAsString(request);

    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

    return new HttpEntity<>(requestJson, jsonHeaders);
  }

  // 테스트 파일을 가져오는 메소드 - 수정됨
  private ByteArrayResource getTestFile() throws IOException {
    Path path = Paths.get(".discodeit/storage/test-message-image.jpg");
    byte[] content = Files.readAllBytes(path);
    return new ByteArrayResource(content) {
      @Override
      public String getFilename() {
        return "test";
      }
    };
  }

  private HttpEntity<ByteArrayResource> getFileEntity(ByteArrayResource file) {
    HttpHeaders profileHeaders = new HttpHeaders();
    profileHeaders.setContentType(MediaType.IMAGE_JPEG);
    return new HttpEntity<>(file,
        profileHeaders);
  }

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    return headers;
  }
}