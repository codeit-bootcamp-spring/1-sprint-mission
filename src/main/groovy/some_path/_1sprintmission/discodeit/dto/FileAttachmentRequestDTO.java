package some_path._1sprintmission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileAttachmentRequestDTO {
    private byte[] data;
    private String contentType;
}
