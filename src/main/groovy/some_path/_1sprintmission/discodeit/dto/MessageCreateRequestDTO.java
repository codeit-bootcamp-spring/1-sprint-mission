package some_path._1sprintmission.discodeit.dto;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class MessageCreateRequestDTO {
    private UUID channelId;
    private UUID authorId;
    private String content;
    private List<FileAttachmentRequestDTO> attachments; // 여러 개의 첨부파일 지원
}
