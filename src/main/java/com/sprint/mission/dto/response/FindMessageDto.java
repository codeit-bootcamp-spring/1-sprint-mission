package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class FindMessageDto {

    private String content;
    private List<byte[]> attachments;

    public FindMessageDto(Message message) {
        this.content = message.getContent();
        this.attachments = message.getAttachments();
    }
}
