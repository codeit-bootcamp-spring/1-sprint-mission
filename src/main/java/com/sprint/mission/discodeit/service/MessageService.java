package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public interface MessageService {
    HashMap<UUID, Message> getChannelMessagesMap(UUID channelId);

    List<MessageDto> findAllMessagesByChannelId(UUID channelId);

    UUID createMessageWithBinaryContents(UUID authorId, UUID channelId, String content, String ...attachmentsPaths);

    UUID createMessage(UUID authorId, UUID channelId, String content);

    Message findMessageById(UUID channelId, UUID messageId);

    boolean reviseMessageContent(UUID channelId, UUID messageId, String content);

    boolean deleteMessage(UUID channelId, UUID messageId);

    boolean isMessageExist(UUID channelId, UUID messageId);

}
