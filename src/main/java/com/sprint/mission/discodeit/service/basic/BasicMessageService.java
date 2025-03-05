package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final Map<UUID, MessageResponse> messages = new HashMap<>();

    @Override
    public MessageResponse create(MessageCreateRequest messageCreateRequest) {
        if (messageCreateRequest.getAuthorId() == null || messageCreateRequest.getChannelId() == null) {
            throw new IllegalArgumentException("AuthorId와 ChannelId는 필수입니다.");
        }

        // 수정: 엔티티 기반 생성자가 있으면 엔티티를 통해 MessageResponse를 생성하는 방식 사용
        // 여기서는 직접 MessageResponse를 생성하는 대신, 생성한 응답 객체를 저장합니다.
        MessageResponse messageResponse = new MessageResponse(
                UUID.randomUUID(),
                messageCreateRequest.getContent(),
                messageCreateRequest.getAuthorId(),
                messageCreateRequest.getChannelId(),
                null  // attachmentIds 등은 null 처리 (생성 후 나중에 업데이트 가능)
        );
        // 현재 시간 저장 (생성 시각 업데이트)
        messageResponse.setCreatedAt(Instant.now());
        messages.put(messageResponse.getId(), messageResponse);
        log.info("✅ 메시지 생성 완료: {}", messageResponse);
        return messageResponse;
    }

    @Override
    public void update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        MessageResponse messageResponse = messages.get(messageId);
        if (messageResponse != null) {
            messageResponse.setContent(messageUpdateRequest.getContent());
            log.info("✅ 메시지 수정 완료: {}", messageId);
        } else {
            log.warn("❌ 메시지 수정 실패: 메시지를 찾을 수 없음.");
        }
    }

    @Override
    public void delete(UUID messageId) {
        messages.remove(messageId);
        log.info("🗑 메시지 삭제 완료: {}", messageId);
    }

    @Override
    public List<MessageResponse> readAllByChannel(UUID channelId) {
        List<MessageResponse> result = new ArrayList<>();
        for (MessageResponse message : messages.values()) {
            if (message.getChannelId().equals(channelId)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<MessageResponse> readAll() {
        return new ArrayList<>(messages.values());
    }
}
