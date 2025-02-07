package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.repository.jcf.JCFMessageRepository;
import com.sprint.mission.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JCFMessageService {

    private final JCFMessageRepository messageRepository;

    public Message create(Message message) {
        return messageRepository.save(message);
    }

    public Message update(UUID messageId, String newMassage){
        Message updatingMessage = findById(messageId);
        updatingMessage.setContent(newMassage);
        return messageRepository.save(updatingMessage);
    }

    //@Override
    public Message findById(UUID messageId){
        return messageRepository.findById(messageId).orElse(null);
    }

    //@Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

//    //@Override
//    public List<Message> findMessagesInChannel(Channel channel) {
//        return messageRepository.findMessagesInChannel(channel);
//    }

    //@Override
    public void delete(UUID messageId) {
        messageRepository.delete(messageId);
    }
}
