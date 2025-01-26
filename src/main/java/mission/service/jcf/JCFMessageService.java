package mission.service.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.repository.jcf.JCFMessageRepository;
import mission.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {

    private final JCFMessageRepository messageRepository = new JCFMessageRepository();

    private static JCFMessageService jcfMessageService;
    private JCFMessageService(){}
    public static JCFMessageService getInstance(){
        if(jcfMessageService == null) return jcfMessageService = new JCFMessageService();
        else return jcfMessageService;
    }

    @Override
    public Message createOrUpdate(Message message) {
        return messageRepository.createOrUpdateMessage(message);
    }

    @Override
    public Message update(UUID messageId, String newMassage){
        Message updatingMessage = findById(messageId);
        updatingMessage.setMessage(newMassage);
        return createOrUpdate(updatingMessage);
    }

    @Override
    public Message findById(UUID messageId){
        return messageRepository.findById(messageId);
    }

    @Override
    public Set<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Set<Message> findMessagesInChannel(Channel channel) {
        return messageRepository.findMessagesInChannel(channel);
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.delete(messageId);
    }
}
