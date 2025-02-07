package com.sprint.mission.service.jcf.main;

import com.sprint.mission.entity.BinaryMessageContent;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFMessageRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.dto.request.BinaryContentDto;
import com.sprint.mission.service.dto.request.MessageDtoForCreate;
import com.sprint.mission.service.dto.request.MessageDtoForUpdate;
import com.sprint.mission.service.jcf.addOn.BinaryMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JCFMessageService {

    private final JCFMessageRepository messageRepository;
    private final JCFChannelRepository channelRepository;
    private final JCFUserRepository userRepository;
    private final BinaryMessageService binaryMessageService;


    public void create(MessageDtoForCreate responseDto) {
        // FindChannelDto byId = channelService.findById(dto.getChannelId());
        // 아직 컨트롤러가 없어서 Sevice 이용하면 DTO 반환...
        Channel writtenChannel = channelRepository.findById(responseDto.getChannelId());
        User writer = userRepository.findById(responseDto.getUserId());

        Message createdMessage = Message.createMessage(writtenChannel, writer, responseDto.getContent());
        if (responseDto.getBinaryMessageContent() != null){
            createdMessage.setBinaryContent(responseDto.getBinaryMessageContent());
            binaryMessageService.create(new BinaryContentDto(createdMessage.getId(), createdMessage.getBinaryContent()));
        }
        messageRepository.save(createdMessage);
    }

    public void update(MessageDtoForUpdate updateDto){
        Message updatingMessage = messageRepository.findById(updateDto.getMessageId());

        BinaryMessageContent bmc = updateDto.getBinaryMessageContent();
        if (bmc != null){
            updatingMessage.setBinaryContent(bmc);
            binaryMessageService.create(new BinaryContentDto(updatingMessage.getId(), bmc));
        }

        updatingMessage.setContent(updateDto.getChangeContent());
        messageRepository.save(updatingMessage);
    }

    //@Override
    public Message findById(UUID messageId){
        return messageRepository.findById(messageId);
    }

    //@Override
    public List<Message> findAllByChannelId(UUID channelId) {
        Channel findChannel = channelRepository.findById(channelId);
        return messageRepository.findAllByChannelId(findChannel);
    }

    //@Override
    public void delete(UUID messageId) {
        binaryMessageService.delete(messageId);
        messageRepository.delete(messageId);
    }
}
