package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.jpa.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusRepository;
import java.util.Arrays;
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
public class ChannelIntegrationTest {


  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private ReadStatusRepository readStatusRepository;

  private final String BASE_URL = "/api/channels";


  @Test
  @DisplayName("공개 채널을 생성할 수 있다.")
  void createPublicChannel() {
    ChannelCreatePublicDTO dto = new ChannelCreatePublicDTO("일반", "소개");

    ResponseEntity<ChannelDto> response = restTemplate.postForEntity(
        BASE_URL + "/public",
        dto,
        ChannelDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody().getName()).isEqualTo("일반");
    assertThat(response.getBody().getType()).isEqualTo(ChannelType.PUBLIC);
  }

  @Test
  @DisplayName("비공개 채널 생성 - DB에 있는 참가자 2명 사용")
  void createPrivateChannelWithPreloadedUsers() throws Exception {
    // given
    UUID user1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    UUID user2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

    ChannelCreatePrivateDTO dto = new ChannelCreatePrivateDTO(List.of(user1, user2));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    String json = objectMapper.writeValueAsString(dto);
    HttpEntity<String> request = new HttpEntity<>(json, headers);

    // when
    ResponseEntity<ChannelDto> response = restTemplate.exchange(
        BASE_URL + "/private",
        HttpMethod.POST,
        request,
        ChannelDto.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getType()).isEqualTo(ChannelType.PRIVATE);

    UUID channelId = response.getBody().getId();

    //ReadStatus 2개가 잘 생성되었는지
    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannel_Id(channelId);
    assertThat(readStatuses).hasSize(2);
    assertThat(readStatuses)
        .extracting(rs -> rs.getUser().getId())
        .containsExactlyInAnyOrder(user1, user2);
  }

  @Test
  @DisplayName("공개 채널을 수정할 수 있다.")
  void updatePublicChannel() {
    // given
    UUID channelId = UUID.fromString("00000000-0000-0000-0000-00000000abcd");

    ChannelUpdateDTO dto = new ChannelUpdateDTO("new name", "new desc");
    HttpEntity<ChannelUpdateDTO> request = new HttpEntity<>(dto);

    // when
    ResponseEntity<ChannelDto> response = restTemplate.exchange(
        BASE_URL + "/" + channelId,
        HttpMethod.PATCH,
        request,
        ChannelDto.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getName()).isEqualTo("new name");
    assertThat(response.getBody().getDescription()).isEqualTo("new desc");
  }

  @Test
  @DisplayName("채널을 삭제할 수 있다.")
  void deleteChannel() {
    // given
    UUID channelId = UUID.fromString("00000000-0000-0000-0000-00000000bbcc");

    // when
    ResponseEntity<Void> response = restTemplate.exchange(
        BASE_URL + "/" + channelId,
        HttpMethod.DELETE,
        null,
        Void.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(channelRepository.existsById(channelId)).isFalse();
  }

  @Test
  @DisplayName("사용자 ID로 채널 목록 조회가 가능하다. - PUBLIC + PRIVATE 포함")
  void findAllChannelsByUserId() {
    // given
    UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

    // when
    ResponseEntity<ChannelDto[]> response = restTemplate.getForEntity(
        BASE_URL + "?userId=" + userId,
        ChannelDto[].class
    );

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(
        5);
    //현재 data-test.sql 에 이 조건에 맞는 채널이 5건이다. 이렇게 테스트 하는게 맞을까???

    List<ChannelType> types = Arrays.stream(response.getBody())
        .map(ChannelDto::getType)
        .toList();

    assertThat(types).contains(ChannelType.PUBLIC, ChannelType.PRIVATE);
  }

}
