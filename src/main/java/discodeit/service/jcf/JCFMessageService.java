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
