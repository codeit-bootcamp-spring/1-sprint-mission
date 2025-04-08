package com.sprint.mission.discodeit.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ChannelIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  private UUID testUserId;
  private User user;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    // 사용자 생성
    user = new User("testUser", "testUser@gmail.com", "password123!", null);
    userRepository.save(user);
    testUserId = user.getId(); // 생성된 사용자 ID를 저장
  }

  @Test
  void testCreatePublicChannel() {
    // 공용 채널 생성
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("Public Channel",
        "Public channel description");
    ResponseEntity<ChannelDto> response = restTemplate.postForEntity("/api/channels/public",
        request, ChannelDto.class);

    assertNotNull(response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("Public Channel", response.getBody().name());
    assertEquals("Public channel description", response.getBody().description());
  }

  @Test
  void testCreatePrivateChannel() {
    // 비공용 채널 생성
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(user.getId()));
    ResponseEntity<ChannelDto> response = restTemplate.postForEntity("/api/channels/private",
        request, ChannelDto.class);

    assertNotNull(response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  void testUpdateChannel() {
    // 공용 채널 생성
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest("Old Channel",
        "Old channel description");
    ResponseEntity<ChannelDto> createResponse = restTemplate.postForEntity("/api/channels/public",
        createRequest, ChannelDto.class);

    UUID channelId = createResponse.getBody().id();

    // 채널 업데이트
    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest("Updated Channel",
        "Updated channel description");
    HttpEntity<PublicChannelUpdateRequest> updateEntity = new HttpEntity<>(updateRequest);
    ResponseEntity<ChannelDto> updateResponse = restTemplate.exchange(
        "/api/channels/{channelId}", HttpMethod.PATCH, updateEntity, ChannelDto.class, channelId);

    assertNotNull(updateResponse.getBody());
    assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
    assertEquals("Updated Channel", updateResponse.getBody().name());
    assertEquals("Updated channel description", updateResponse.getBody().description());
  }

  @Test
  void testDeleteChannel() {
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest(
        "Channel to be deleted", "Description");
    ResponseEntity<ChannelDto> createResponse = restTemplate.postForEntity("/api/channels/public",
        createRequest, ChannelDto.class);

    UUID channelId = createResponse.getBody().id();

    ResponseEntity<ChannelDto> findResponse = restTemplate.exchange(
        "/api/channels/{channelId}", HttpMethod.DELETE, null, ChannelDto.class, channelId);
    assertEquals(HttpStatus.NO_CONTENT, findResponse.getStatusCode());
  }

  @Test
  void testFindAllChannelsByUserId() {
    // 사용자 채널 생성
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest("User's Channel",
        "User's channel description");
    restTemplate.postForEntity("/api/channels/public", createRequest, ChannelDto.class);

    // 사용자 채널 목록 조회
    ResponseEntity<List<ChannelDto>> findResponse = restTemplate.exchange(
        "/api/channels?userId=" + testUserId, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<ChannelDto>>() {
        });

    assertNotNull(findResponse.getBody());
    assertTrue(findResponse.getBody().size() > 0);
    assertEquals(HttpStatus.OK, findResponse.getStatusCode());
  }
}