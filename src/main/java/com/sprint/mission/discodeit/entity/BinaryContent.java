package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class BinaryContent implements Serializable {
    private String id;
    private String userId;
    private byte[] content;

    public BinaryContent(String userId, byte[] content) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.content = content;
    }
}