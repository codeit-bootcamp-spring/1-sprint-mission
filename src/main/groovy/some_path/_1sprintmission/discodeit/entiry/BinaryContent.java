package some_path._1sprintmission.discodeit.entiry;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private final UUID id;
    private final Instant createdAt;

    private final byte[] data; // 파일 저장을 위한 바이너리 데이터

    private String contentType; // MIME 타입 (예: image/png, application/pdf 등)

    private User uploadedBy; //누가 올린 글인지

    private Message message; //채널, 유저, content

    public BinaryContent(byte[] data, String contentType, User uploadedBy, Message message) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.data = data;
        this.contentType = contentType;
        this.uploadedBy = uploadedBy;
        this.message = message;
    }
}
