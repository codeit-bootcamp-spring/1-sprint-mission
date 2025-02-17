package com.sprint.mission.discodeit.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessaageInChannelDto {
    private String channel;
    private String sender;
    private String receiver;
    private String message;
}
