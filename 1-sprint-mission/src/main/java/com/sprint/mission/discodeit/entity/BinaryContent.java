package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 3982777564854187326L;
    private UUID userId;
    private UUID messageId;
    private Instant createdAt;
    private String contentUrl;
    private List<String> contentUrls;


    @Override
    public String toString() {
        String print = "";
        print = "BinaryContent{";
        if(userId != null) print += "userId=" + userId;

        if(messageId != null) print = ", createdAt=" + createdAt;
        print += '}';

        return print;
    }
}
