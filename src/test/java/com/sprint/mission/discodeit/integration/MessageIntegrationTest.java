package com.sprint.mission.discodeit.integration;


import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.repository.jpa.MessageRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MessageIntegrationTest {

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MessageRepository messageRepository;

  private final String BASE_URL = "/api/messages";

  @Test
  @DisplayName("메시지를 생성할 수 있다.")
  void createMessageWithoutAttachment() throws Exception {
    // given
    UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    UUID channelId = UUID.fromString("00000000-0000-0000-0000-00000000aaaa");

    MessageCreateDTO dto = new MessageCreateDTO("안녕하세요!", channelId, userId);
    String json = objectMapper.writeValueAsString(dto);

    HttpHeaders jsonHeader = new HttpHeaders();
    jsonHeader.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> jsonPart = new HttpEntity<>(json, jsonHeader);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("messageCreateRequest", jsonPart);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

    // when
    ResponseEntity<MessageDto> response = restTemplate.postForEntity(
        BASE_URL, request, MessageDto.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getContent()).isEqualTo("안녕하세요!");
  }

  @Test
  @DisplayName("메시지를 수정할 수 있다.")
  void updateMessage() {
    // given
    UUID messageId = UUID.fromString("00000000-0000-0000-0000-00000000dddd");
    MessageUpdateDTO dto = new MessageUpdateDTO("수정된 메시지입니다");
    HttpEntity<MessageUpdateDTO> request = new HttpEntity<>(dto);

    // when
    ResponseEntity<MessageDto> response = restTemplate.exchange(
        BASE_URL + "/" + messageId,
        HttpMethod.PATCH,
        request,
        MessageDto.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getContent()).isEqualTo("수정된 메시지입니다");
  }

  @Test
  @DisplayName("메시지를 삭제할 수 있다.")
  void deleteMessage() {
    // given
    UUID messageId = UUID.fromString("00000000-0000-0000-0000-00000000eeee");

    // when
    ResponseEntity<Void> response = restTemplate.exchange(
        BASE_URL + "/" + messageId,
        HttpMethod.DELETE,
        null,
        Void.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(messageRepository.existsById(messageId)).isFalse();
  }

  @Test
  @DisplayName("채널 ID로 메시지를 조회할 수 있다.")
  void findAllMessagesByChannelId_parsed() throws Exception {
    // given
    UUID channelId = UUID.fromString("00000000-0000-0000-0000-00000000aaaa");

    // when
    ResponseEntity<String> response = restTemplate.getForEntity(
        BASE_URL + "?channelId=" + channelId,
        String.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    // then: PageResponse<MessageDto>로 파싱
    JavaType type = objectMapper.getTypeFactory()
        .constructParametricType(PageResponse.class, MessageDto.class);

    PageResponse<MessageDto> page = objectMapper.readValue(response.getBody(), type);

    // 검증
    assertThat(page).isNotNull();
    assertThat(page.getContent()).hasSize(2); // 데이터 넣은 대로 검증
    assertThat(page.getContent().get(0).getContent()).isNotBlank();
  }

}
