package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasicMessageService implements MessageService {
    @Autowired
    private final MessageRepository messageRepository;
    public BasicMessageService(MessageRepository messageRepository){this.messageRepository = messageRepository;}

    @Override
    public boolean createMessage(Message message) {
        boolean created = messageRepository.save(message);
        if(created){
            System.out.println("생성된 메세지: " + message);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public Optional<Message> readMessage(UUID id) {
        Optional<Message> msg = messageRepository.findById(id);
        msg.ifPresent(m -> System.out.println("조회된 메세지: " + m));
        return msg;
    }

    @Override
    public List<Message> readAllMessages() {
        List<Message> msgs = messageRepository.findAll();
        if(msgs != null && !msgs.isEmpty()){
            System.out.println("전체 메세지 목록: " + msgs);
            return msgs;
        } else {
            System.out.println("메세지 목록이 비어 있습니다.");
            return Collections.emptyList(); // 비어 있을 경우 빈 리스트 반환
        }
    }

    @Override
    public void updateMessage(UUID id, String content, UUID authorId) {
        boolean updated = messageRepository.updateOne(id, content, authorId);
        if (updated) {
            Optional<Message> msg = messageRepository.findById(id);
            msg.ifPresent(m -> System.out.println("수정된 메세지: " + m));
            List<Message> allMessages = messageRepository.findAll();
            System.out.println("수정 후 전체 메세지 목록: " + allMessages);
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        boolean deleted = messageRepository.deleteOne(id);
        if (deleted){
            Optional<Message> msg = messageRepository.findById(id);
            System.out.println("삭제된 메세지: " + msg);
            List<Message> allMessages = messageRepository.findAll();
            System.out.println("삭제 후 전체 메세지 목록: " + allMessages);
        }
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}