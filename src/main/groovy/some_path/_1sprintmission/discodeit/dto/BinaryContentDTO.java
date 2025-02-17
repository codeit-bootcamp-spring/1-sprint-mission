package some_path._1sprintmission.discodeit.dto;

import java.util.UUID;

public class BinaryContentDTO {
    private byte[] data;
    private String contentType;
    private UUID uploadedById;
    private UUID messageId;

    public BinaryContentDTO(byte[] data, String contentType, UUID uploadedById, UUID messageId) {
        this.data = data;
        this.contentType = contentType;
        this.uploadedById = uploadedById;
        this.messageId = messageId;
    }

    public byte[] getData() {
        return data;
    }

    public String getContentType() {
        return contentType;
    }

    public UUID getUploadedById() {
        return uploadedById;
    }

    public UUID getMessageId() {
        return messageId;
    }
}

