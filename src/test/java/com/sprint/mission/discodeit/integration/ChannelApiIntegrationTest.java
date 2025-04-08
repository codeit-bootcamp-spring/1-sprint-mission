package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
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

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChannelApiIntegrationTest {

  @Autowired
  private ChannelService channelService;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("공개 채널 생성 성공")
  void createPubCh() throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("ch1", "pubCh");
    HttpEntity<String> jsonEntity = getJsonEntity(request);

    ResponseEntity<ChannelDto> response = restTemplate.exchange(
        "/api/channels/public",
        HttpMethod.POST,
        jsonEntity,
        ChannelDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().name()).isEqualTo(request.name());
    assertThat(response.getBody().description()).isEqualTo(request.description());
  }

  @Test
  @DisplayName("공개 채널 생성 실패 - 채널명이 비어있음")
  void FailCreatePublicChannelByName() throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(null, "pubCh");
    HttpEntity<String> jsonEntity = getJsonEntity(request);

    ResponseEntity<ChannelDto> response = restTemplate.exchange(
        "/api/channels/public",
        HttpMethod.POST,
        jsonEntity,
        ChannelDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("공개 채널 생성 실패 - 채널 설명이 비어있음")
  void FailCreatePublicChannelByDescription() throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("ch1", null);
    HttpEntity<String> jsonEntity = getJsonEntity(request);

    ResponseEntity<ChannelDto> response = restTemplate.exchange(
        "/api/channels/public",
        HttpMethod.POST,
        jsonEntity,
        ChannelDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("비공개 채널 생성 성공")
  void createPriCh() throws Exception {
    List<User> users = userRepository.findAll();
    List<UUID> ids = users.stream()
        .map(BaseEntity::getId)
        .toList();
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(ids);
    HttpEntity<String> jsonEntity = getJsonEntity(request);

    ResponseEntity<ChannelDto> response = restTemplate.exchange(
        "/api/channels/private",
        HttpMethod.POST,
        jsonEntity,
        ChannelDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(3).isEqualTo(response.getBody().participants().size());
  }

  @Test
  @DisplayName("공개 채널 삭제")
  void removePubChannel() throws Exception {
    UUID channelId = UUID.fromString("6f17a8d1-77d7-437d-811e-d98db3bd30bc");

    ResponseEntity<Void> response = restTemplate.exchange(
        "/api/channels/{channelId}",
        HttpMethod.DELETE,
        null,
        Void.class,
        channelId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("비공개 채널 삭제")
  void removePriChannel() throws Exception {
    UUID channelId = UUID.fromString("0017a8d1-77d7-437d-811e-d98db3bd30bc");

    ResponseEntity<Void> response = restTemplate.exchange(
        "/api/channels/{channelId}",
        HttpMethod.DELETE,
        null,
        Void.class,
        channelId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("공개 채널 수정 성공")
  void modifyPubChannel() throws Exception {
    UUID channelId = UUID.fromString("6f17a8d1-77d7-437d-811e-d98db3bd30bc");
    ChannelModifyRequest request = new ChannelModifyRequest("newCh", "new channel");
    HttpEntity<String> jsonEntity = getJsonEntity(request);

    ResponseEntity<ChannelDto> response = restTemplate.exchange(
        "/api/channels/{channelId}",
        HttpMethod.PATCH,
        jsonEntity,
        ChannelDto.class,
        channelId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat("newCh").isEqualTo(response.getBody().name());
    assertThat("new channel").isEqualTo(response.getBody().description());
  }

  @Test
  @DisplayName("비공개 채널은 수정 못함")
  void doNotModifyPrivateChannel() throws Exception {
    UUID channelId = UUID.fromString("3417a8d1-77d7-437d-811e-d98db3bd30bc");

    ChannelModifyRequest request = new ChannelModifyRequest("newCh", "new channel");
    HttpEntity<String> jsonEntity = getJsonEntity(request);

    ResponseEntity<ChannelDto> response = restTemplate.exchange(
        "/api/channels/{channelId}",
        HttpMethod.PATCH,
        jsonEntity,
        ChannelDto.class,
        channelId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private HttpEntity<String> getJsonEntity(Object request) throws JsonProcessingException {
    String requestJson = objectMapper.writeValueAsString(request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(requestJson, headers);
  }
}