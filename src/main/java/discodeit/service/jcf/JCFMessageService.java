package discodeit.service.jcf;

import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.MessageService;

import java.util.UUID;

public class JCFMessageService implements MessageService {
    
    @Override
    public void createMessage(String content, User sender) {

    }

    @Override
    public UUID readId(Message message) {
        return null;
    }

    @Override
    public long getCreateAt(Message message) {
        return 0;
    }

    @Override
    public long getUpdatedAt(Message message) {
        return 0;
    }

    @Override
    public void getInfo(Message message) {

    }

    @Override
    public String getContent(Message message) {
        return "";
    }

    @Override
    public User getSender(Message message) {
        return null;
    }

    @Override
    public void updateContent(Message message) {

    }

    @Override
    public void deleteMessage(Message message) {

    }
}
