package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateDTO;
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

    private final Map<UUID, MessageDTO> messages = new HashMap<>();

    @Override
    public MessageDTO create(MessageCreateDTO messageCreateDTO) {
        if (messageCreateDTO.getSenderId() == null || messageCreateDTO.getChannelId() == null) {
            throw new IllegalArgumentException("SenderId와 ChannelId는 필수입니다.");
        }

        // ✅ 메시지 생성 시 현재 시간을 저장
        MessageDTO messageDTO = new MessageDTO(
                UUID.randomUUID(),
                messageCreateDTO.getContent(),
                messageCreateDTO.getSenderId(),
                messageCreateDTO.getChannelId(),
                Instant.now()  // ✅ 현재 시간을 저장
        );

        messages.put(messageDTO.getId(), messageDTO);
        log.info("✅ 메시지 생성 완료: {}", messageDTO);
        return messageDTO;
    }

    @Override
    public void update(UUID messageId, MessageUpdateDTO messageUpdateDTO) {
        MessageDTO messageDTO = messages.get(messageId);
        if (messageDTO != null) {
            messageDTO.setContent(messageUpdateDTO.getContent());
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
    public List<MessageDTO> readAllByChannel(UUID channelId) {
        List<MessageDTO> result = new ArrayList<>();
        for (MessageDTO message : messages.values()) {
            if (message.getChannelId().equals(channelId)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<MessageDTO> readAll() {
        return new ArrayList<>(messages.values());
    }
}