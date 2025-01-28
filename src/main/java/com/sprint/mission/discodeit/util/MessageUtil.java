package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.exception.validation.message.InvalidMessageException;

public class MessageUtil {
    public static void validMessageId(Message message) {
        if (message.getId() == null) {
            throw new NotfoundIdException("유효하지 않은 id입니다.");
        }
    }

    public static void validContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new InvalidMessageException("유효하지 않은 문장입니다.");
        }
        if (content.length() > 100) {
            throw new InvalidMessageException("너무 긴 문장입니다.");
        }
    }
}
