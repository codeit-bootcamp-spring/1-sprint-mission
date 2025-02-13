package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID ownerId;
    private byte[] data;
    private String fileType;

    @Override
    public String toString() {
        return "BinaryContent{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
