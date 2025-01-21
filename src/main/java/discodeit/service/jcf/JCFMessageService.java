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

    private final List<Message> messages;
    private UserService jcfUserService;
    private ChannelService jcfChannelService;

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
    public Message createMessage(Channel channel, String content, User sender) {
        Message newMessage = new Message(content, sender);
        messages.add(newMessage);
        jcfChannelService.updateMessages(channel, newMessage);
        return newMessage;
    }

    @Override
    public Message findById(UUID id) {
        Message findMessage = findMessage(id);
        if (findMessage == null) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다.");
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
    public void deleteMessage(Message message, Channel channel, User user) {
        channel.deleteMessage(message, user);
        messages.remove(message);
    }

    @Override
    public void deleteAllMessages(List<Message> deleteMessages) {
        for (Message deleteMessage : deleteMessages) {
            messages.remove(deleteMessage);
        }
    }
}
