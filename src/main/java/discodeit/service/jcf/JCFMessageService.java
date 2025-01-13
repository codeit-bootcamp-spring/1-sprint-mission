package discodeit.service.jcf;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private UserService jcfUserService;
    private ChannelService jcfChannelService;
    private final List<Message> messages;

    private JCFMessageService() {
        messages = new ArrayList<>();
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
        Message newMessage = new Message(content, sender);
        messages.add(newMessage);
        return newMessage;
    }

    @Override
    public Message findById(UUID id) {
        Message findMessage = findMessage(id);
        if (findMessage == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        return findMessage;
    }

    @Override
    public Message findMessage(UUID id) {
        return messages.stream()
                .filter(message -> message.isIdEqualTo(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public String getInfo(Message message) {
        return message.toString();
    }

    @Override
    public void updateContent(Message message, String content) {
        message.updateContent(content);
        message.updateUpdatedAt();
    }

    @Override
    public void deleteMessage(Channel channel, Message message) {
        channel.deleteMessage(message);
        messages.remove(message);
    }
}
