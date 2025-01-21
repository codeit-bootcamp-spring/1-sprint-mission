package discodeit.service.jcf;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> messages;
    private UserService jcfUserService;
    private ChannelService jcfChannelService;

    private JCFMessageService() {
        messages = new HashMap<>();
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
        Message message = new Message(content, sender);
        messages.put(message.getId(), message);
        jcfChannelService.updateMessages(channel, message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message foundMessage = messages.get(messageId);

        return Optional.ofNullable(foundMessage)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 메시지입니다."));
    }

    @Override
    public List<Message> findAll() {
        return messages.values().stream().toList();
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
