package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMassageService implements MessageService {

    @Autowired
    private final MessageRepository messageRepository;
    @Autowired
    private BinaryContentService binaryContentService;


    @Override
    public Message createMessage(CreateMessageRequest request) {
        Message message=new Message(request.getContent(),request.getChannelId(),request.getAuthorId());
        Message savedMessage=messageRepository.save(message);
        if(request.getAttachments()!=null){
            for (byte[] fileData : request.getAttachments()) {
                BinaryContentCreateRequest binaryContentCreateRequest=new BinaryContentCreateRequest(request.getAuthorId(),fileData);
                binaryContentService.createProfile(binaryContentCreateRequest);
            }
        }
        return savedMessage;
    }

    @Override
    public Optional<Message> getMessageById(UUID id) {
        return Optional.ofNullable(messageRepository.getMessageById(id)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + id + " not fount")));

    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.getAllMessage();
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.getAllMessage()
                .stream().filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public Message updateMessage(UpdateMessageRequest request) {
        Message message = messageRepository.getMessageById(request.getMessageId())
                .orElseThrow(() -> new NoSuchElementException("Message with id " + request.getMessageId() + " not found"));
        message.update(request.getNewContent());
        return messageRepository.save(message);
    }


    @Override
    public void deleteMessage(UUID id) {
        binaryContentService.deleteByMessageId(id);
        messageRepository.deleteMessage(id);
    }

    @Override
    public void deleteByChannelId(UUID channelID) {
        messageRepository.deleteByChannelId(channelID);
    }

}
