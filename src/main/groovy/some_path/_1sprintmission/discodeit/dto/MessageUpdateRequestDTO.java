package some_path._1sprintmission.discodeit.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageUpdateRequestDTO {
    private final UUID messageId;
    private final String content; // 수정할 메시지 내용

    public MessageUpdateRequestDTO(UUID messageId, String content) {
        this.messageId = messageId;
        this.content = content;
    }
}
