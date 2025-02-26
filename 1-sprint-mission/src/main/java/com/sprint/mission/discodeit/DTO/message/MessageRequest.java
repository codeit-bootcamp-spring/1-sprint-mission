package com.sprint.mission.discodeit.DTO.message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageRequest {
    private String sender;
    private String receiver;
    private String message;

}