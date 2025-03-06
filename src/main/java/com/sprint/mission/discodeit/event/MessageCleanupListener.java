package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageCleanupListener {
    private final BinaryContentService binaryContentService;

    @EventListener
    public void handlerMessageDeleted(MessageDeletedEvent event) {

        Message message = event.message();

        // 메시지 관련 파일 삭제
        if (message.getImagesId() != null) {
            for (UUID id : message.getImagesId()) {
                binaryContentService.delete(id);
            }
        }
    }
}
