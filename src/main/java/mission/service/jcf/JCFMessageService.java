package mission.service.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.repository.jcf.JCFMessageRepository;
import mission.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {

    private final JCFMessageRepository messageRepository = new JCFMessageRepository();

    @Override
    public Message createOrUpdate(Message message) {
        messageRepository.createOrUpdateMessage(message);
        return message;
    }

    @Override
    public Message update(UUID messageId, String newMassage){
        Message updatingMessage = findById(messageId);
        updatingMessage.setMessage(newMassage);
        return createOrUpdate(updatingMessage);
    }

    /**
     * 조회
     */

    @Override
    public Set<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message findById(UUID messageId){
        return messageRepository.findById(messageId);
    }

    @Override
    public Set<Message> findMessagesInChannel(Channel channel) {
        return messageRepository.findMessagesInChannel(channel);
    }

    public Set<Message> findMessagesByContentInChannel(Channel channel, String findMessage){
        Set<Message> messagesInChannel = findMessagesInChannel(channel);
        return messagesInChannel.stream()
                .filter(message -> message.getMessage().contains(findMessage))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.delete(messageId);
    }
}
