package mission.service.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.repository.jcf.JCFMessageRepository;
import mission.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final JCFMessageRepository messageRepository = new JCFMessageRepository();

    @Override
    public Message create(Channel writeAt, User wirter, String writedMessage) {
        return messageRepository.createMessage(writeAt, wirter, writedMessage);
    }

    @Override
    public List<Message> findMessagesInChannel(Channel channel) {
        return messageRepository.findMessagesInChannel(channel);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public Message findMessage(User writer, String writedMessage){
        for (Message message : writer.getMessages()) {
            if (message.getMessage().equals(writedMessage)){
                return message;
            }
        }
        return null;
    }

    @Override
    public Message update(UUID messageId, String newMessage) {
        return messageRepository.updateMessage(messageId, newMessage);
    }

    public void delete(Channel writedAt, UUID messageId, User writer) {
        Message deletingMessage = messageRepository.findMessageById(messageId);
        messageRepository.delete(writedAt, deletingMessage, writer);
    }
}
