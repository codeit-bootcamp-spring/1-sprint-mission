package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicMessageService implements MessageService {
    private MessageRepository messageRepository;
    private UserService userService;
    private ChannelService channelService;

    @Autowired
    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public void setService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void createMessage(Message message) {
        if (!isContent(message.getContent())) {
            return;
        }
        Message newMessage = new Message(message);
        messageRepository.save(newMessage);
        System.out.println("새로운 메시지가 작성되었습니다.");
    }

    @Override
    public List<Message> getAllMessageList() {
        return messageRepository.findAll().values().stream().collect(Collectors.toList());
    }

    @Override
    public Message searchById(UUID messageId) {
        Message message = messageRepository.findById(messageId);
        if(message == null) {
            System.out.println("해당 메세지가 존재하지 않습니다.");
        }
        return message;
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = searchById(messageId);
        if(message == null) {
            System.out.println("해당 메세지가 존재하지 않습니다,");
        } else {
            messageRepository.delete(messageId);
            System.out.println("해당 메세지가 삭제되었습니다.");
        }

    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        if(!isContent(newContent)) {
            return;
        }
        try {
            Message message = searchById(messageId);
            message.setContent(newContent);
            messageRepository.save(message);
            System.out.println(newContent+ "로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("해당 메세지가 존재하지 않습니다.");
            e.printStackTrace();
        }
    }

    private boolean isContent(String content) {
        if (content == null || content.isBlank()) {
            System.out.println("내용을 입력해주세요!!!");
            return false;
        }
        return true;
    }
}