package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    Message create(Message newMessage);
    Message readById(String messageId);
    List<Message> readAll();
    Message updateMessage(String messageId, Message updateMessage);
    void delete(String messageId);
    Channel getChannel(String messageId);
}
