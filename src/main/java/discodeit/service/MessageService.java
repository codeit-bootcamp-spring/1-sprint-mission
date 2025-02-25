package discodeit.service;

import discodeit.dto.message.CreateMessageRequest;
import discodeit.dto.message.UpdateMessageRequest;
import discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(CreateMessageRequest createMessageRequest);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(UpdateMessageRequest updateMessageRequest);
    void delete(UUID messageId);
}