package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    //Repository 의존성 주입
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public JCFMessageService(MessageRepository messageRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }
    //생성
    @Override
    public Message create(String content,UUID channelId,UUID userId) {
        Channel channel;
        User user;
        try{
            channel = channelRepository.findById(channelId);
            user = userRepository.findById(userId);
        }catch (NoSuchElementException e){
            throw e;
        }
        Message message = new Message(content,channel.getId(),user.getId()); //파라미터로 받은 값으로 객체 생성
        return messageRepository.create(message);
    }

    // 단일조회
    @Override
    public Message find(UUID messageId) {
        try{ //id값이 null이 아니면 그대로 리턴
            Message messageNullable = messageRepository.findById(messageId);
            if(messageNullable == null){
                throw new NoSuchElementException("MessageId not found");
            }
            return messageNullable;
        }catch (NoSuchElementException e){
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    //다중조회
    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        try{
            //객체가 null 이면 업데이트 대상 존재x
            Message messageNullable = messageRepository.findById(messageId);
            if(messageNullable == null){
                throw new NoSuchElementException("MessageId not found");
            }
            messageNullable.updateContent(newContent);
            return messageNullable;
        }catch (NoSuchElementException e){
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(UUID messageId) {
        if(messageRepository.findById(messageId) == null){
            throw new NoSuchElementException("MessageId not found ");
        }
        messageRepository.delete(messageId);
    }
}
