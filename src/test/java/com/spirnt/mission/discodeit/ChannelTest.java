package com.spirnt.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.spirnt.mission.discodeit.dto.ErrorResponse;
import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.spirnt.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChannelTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Transactional
    @DisplayName("공개 채널 생성 테스트 - 성공")
    void testCreatePublicChannelSuccess() {
        PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
            "ch 1",
            "this is ch 1");
        ResponseEntity<ChannelDto> response = restTemplate.postForEntity("/api/channels/public",
            publicChannelCreateRequest, ChannelDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("ch 1", response.getBody().getName());
        assertEquals("this is ch 1", response.getBody().getDescription());
        assertEquals(ChannelType.PUBLIC, response.getBody().getType());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @Transactional
    @DisplayName("공개 채널 생성 테스트 - 실패")
    void testCreatePublicChannelFail() {
        PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(null,
            "this is ch 1");
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/api/channels/public",
            publicChannelCreateRequest, ErrorResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getExceptionType());
    }

    @Test
    @Transactional
    @DisplayName("비공개 채널 생성 테스트 - 성공")
    void testCreatePrivateChannel() {
        PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(
            List.of(
                UUID.fromString("00000000-0000-0000-0000-000000000103"), UUID.fromString(
                    "00000000-0000-0000-0000-000000000104")));
        ResponseEntity<ChannelDto> response = restTemplate.postForEntity("/api/channels/private",
            privateChannelCreateRequest, ChannelDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ChannelType.PRIVATE, response.getBody().getType());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @Transactional
    @DisplayName("비공개 채널 생성 테스트 - 실패")
    void testCreatePrivateChannelFail() {
        PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(
            List.of(
                UUID.fromString("00000000-0000-0000-0000-000000111101"), UUID.fromString(
                    "00000000-0000-0000-0000-000000000102")));

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/api/channels/private",
            privateChannelCreateRequest, ErrorResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("UserNotFoundException", response.getBody().getExceptionType());
    }

    @Test
    @Transactional
    @DisplayName("채널 수정 테스트 - 성공")
    void testUpdateChannelSuccess() {
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest(
            "new ch 1", "new description");
        UUID channelId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        ResponseEntity<ChannelDto> response = restTemplate.exchange(
            "/api/channels/" + channelId,
            HttpMethod.PATCH,
            new HttpEntity<>(publicChannelUpdateRequest),
            ChannelDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("new ch 1", response.getBody().getName());
        assertEquals("new description", response.getBody().getDescription());
    }

    @Test
    @Transactional
    @DisplayName("채널 수정 테스트 - 실패")
    void testUpdateChannelFail() {
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest(
            "new ch 1", "new description");
        // 존재하지 않는 id
        UUID channelId = UUID.randomUUID();

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/channels/" + channelId,
            HttpMethod.PATCH,
            new HttpEntity<>(publicChannelUpdateRequest),
            ErrorResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ChannelNotFoundException", response.getBody().getExceptionType());
    }

    @Test
    @Transactional
    @DisplayName("채널 삭제 테스트 - 성공")
    void testDeleteChannelSuccess() {
        UUID channelId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        ResponseEntity<Void> response = restTemplate.exchange(
            "/api/channels/" + channelId,
            HttpMethod.DELETE,
            null,
            Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Transactional
    @DisplayName("채널 삭제 테스트 - 실패")
    void testDeleteChannelFail() {
        UUID channelId = UUID.fromString("11111111-1111-1111-1111-111111111222");

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/channels/" + channelId,
            HttpMethod.DELETE,
            null,
            ErrorResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ChannelNotFoundException", response.getBody().getExceptionType());
    }

}
