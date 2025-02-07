package com.sprint.mission.service.jcf.main;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFMessageRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.dto.request.MessageDtoForCreate;
import com.sprint.mission.service.dto.request.MessageDtoForUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JCFMessageService {

    private final JCFMessageRepository messageRepository;
    private final JCFChannelRepository channelRepository;
    private final JCFUserRepository userRepository;


    public void create(MessageDtoForCreate responseDto) {
        // FindChannelDto byId = channelService.findById(dto.getChannelId());
        // 아직 컨트롤러가 없어서 Sevice 이용하면 DTO 반환...
        Channel writtenChannel = channelRepository.findById(responseDto.getChannelId());
        User writer = userRepository.findById(responseDto.getUserId());

        Message createdMessage = Message.createMessage(writtenChannel, writer, responseDto.getContent());
        messageRepository.save(createdMessage);
    }

    public void update(MessageDtoForUpdate updateDto){
        Message updatingMessage = messageRepository.findById(updateDto.getMessageId()).orElseThrow(() -> new NoSuchElementException("Cannot find Message : incorrect MessageId"));

        updatingMessage.setContent(updateDto.getChangeContent());
        updatingMessage.setBinaryContent(updateDto.getBinaryMessageContent());

        messageRepository.save(updatingMessage);
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
