package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MessageDto {
    Boolean haveContent;
    String text;
    UUID channelId;
    String channelName;
    UUID messageId;
    UUID userId;
    String userName;
    String newContent;
    List<String> contentUrl;
}
