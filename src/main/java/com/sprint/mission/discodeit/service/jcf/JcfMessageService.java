package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

import static com.sprint.mission.discodeit.util.MyLogger.log;

public class JcfMessageService implements MessageService {

    private final MessageRepository messageRepository=new JcfMessageRepository();
    @Override
    public void messageSave(Message message) {
        if (message.getContent().trim().isEmpty()) {
            log("메세지 내용을 입력해주세요.");
            return;
        }
        messageRepository.createMessage(message.getId(), message);
    }

    @Override
    public Optional<Message> findMessage(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMessage(UUID id,String updateMessage) {
        if(messageRepository.findById(id).isPresent()) {
            messageRepository.updateMessage(id, updateMessage);
        }else {
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다");
        }
    }


    @Override
    public void deleteMessage(UUID id) {
        if(messageRepository.findById(id).isPresent()) {
            messageRepository.deleteMessage(id);
        }else {
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다");
        }
    }
}
