package discodeit.service.jcf;

import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.UUID;

public class JCFMessageService implements MessageService {

    private UserService jcfUserService;
    private ChannelService jcfChannelService;

    private JCFMessageService() {
    }

    private static class JCFMessageServiceHolder {
        private static final MessageService INSTANCE = new JCFMessageService();
    }

    public static MessageService getInstance() {
        return JCFMessageServiceHolder.INSTANCE;
    }

    @Override
    public void updateUserService(UserService jcfUserService) {
        this.jcfUserService = jcfUserService;
    }

    @Override
    public void updateChannelService(ChannelService jcfChannelService) {
        this.jcfChannelService = jcfChannelService;
    }

    @Override
    public Message createMessage(String content, User sender) {
        return new Message(content, sender);
    }

    @Override
    public UUID readId(Message message) {
        return message.getId();
    }

    @Override
    public long getCreateAt(Message message) {
        return message.getCreatedAt();
    }

    @Override
    public long getUpdatedAt(Message message) {
        return message.getUpdatedAt();
    }

    @Override
    public String getInfo(Message message) {
        return message.toString();
    }

    @Override
    public String getContent(Message message) {
        return message.getContent();
    }

    @Override
    public User getSender(Message message) {
        return  message.getSender();
    }

    @Override
    public void updateContent(Message message, String content) {
        message.updateContent(content);
        message.updateUpdatedAt();
    }

    @Override
    public void deleteMessage(Message message) {

    }
}
