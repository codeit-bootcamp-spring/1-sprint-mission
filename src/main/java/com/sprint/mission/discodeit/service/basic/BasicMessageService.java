package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
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
    public Message createMessage(MessageCreateDTO messageCreateDTO) {
        Message message = new Message(messageCreateDTO);
        return  messageRepository.save(message);
    }

    @Override
    public Message find(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> messageList = messageRepository.findByChannelId(channelId);
        return messageList;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messageList= new ArrayList<>( messageRepository.load().values());
        return messageList;
    }

    @Override
    public Message update(MessageUpdateDTO messageUpdateDTO) {
        Message message = messageRepository.findById(messageUpdateDTO.uuid());
        message.updateContent(messageUpdateDTO);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID msgID) {
        //관련된 도메인 삭제 Binary
        Message message = messageRepository.findById(msgID);
        message.deleteBinaryContentList();;
        messageRepository.delete(msgID);
    }
}
