package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        Message message = new Message(userId, channelId, content);
        return  messageRepository.save(message);
    }

    @Override
    public Message readMessage(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> readAllMessage() {
        List<Message> messageList= new ArrayList<>( messageRepository.load().values());
        return messageList;
    }

    @Override
    public Message modifyMessage(UUID msgID, String content) {
        Message message = messageRepository.findById(msgID);
        message.updateContent(content);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID msgID) {
        messageRepository.delete(msgID);
    }
}
