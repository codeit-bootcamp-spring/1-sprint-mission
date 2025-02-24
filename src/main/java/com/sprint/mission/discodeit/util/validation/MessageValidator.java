package com.sprint.mission.discodeit.util.validation;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageValidator implements Validator<Message, MessageDTO.request> {
    @Override
    public void validateCreate(MessageDTO.request entity) {
        if (entity.author() == null) {
            throw new CustomException(ErrorCode.AUTHOR_CANNOT_BLANK);
        }
        if (entity.channel() == null) {
            throw new CustomException(ErrorCode.CHANNEL_CANNOT_BLANK);
        }
        if (entity.content() == null) {
            throw new CustomException(ErrorCode.CONTENT_CANANOT_BLANK);
        }
    }

    @Override
    public Message validateUpdate(Message current, MessageDTO.request update) {
        boolean isUpdated = false;
        if (update.content() != null && !update.content().equals(current.getContent())) {
            current.updateContent(update.content());
            isUpdated = true;
        }
        return isUpdated ? current : null;
    }
}
