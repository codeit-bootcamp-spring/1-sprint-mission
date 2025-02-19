package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMassageService implements MessageService {

    @Autowired
    private final MessageRepository messageRepository;
    @Autowired
    private final BinaryContentService binaryContentService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ChannelRepository channelRepository;


    @Override
    public Message createMessage(CreateMessageRequestDto request)  {
        if(!channelRepository.existsById(request.getChannelId())) {
            throw new NoSuchElementException("Channel not found");
        }
        if(!userRepository.existsById(request.getAuthorId())){
            throw new NoSuchElementException("User not found");
        }
        Message message=new Message(request.getContent(),request.getChannelId(),request.getAuthorId());
        Message savedMessage=messageRepository.save(message);
        if(request.getAttachments()!=null){
            for (MultipartFile fileData : request.getAttachments()) {
                if (!fileData.isEmpty()) {
                    BinaryContentCreateRequestDto binaryContentCreateRequestDto =new BinaryContentCreateRequestDto(null,request.getAuthorId(),fileData);
                    binaryContentService.createProfile(binaryContentCreateRequestDto);
                }
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
    public Message updateMessage(UUID id,UpdateMessageRequestDto request) {
        Message message = messageRepository.getMessageById(id)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + id + " not found"));
        message.update(request.getNewContent());
        return messageRepository.save(message);
    }


    @Override
    public void deleteMessage(UUID id) {
        if(!messageRepository.existsById(id)) {
            throw new NoSuchElementException("Message with id " + id + " not fount");
        }
        binaryContentService.deleteByMessageId(id);
        messageRepository.deleteMessage(id);
    }

    @Override
    public void deleteByChannelId(UUID channelID) {
        messageRepository.deleteByChannelId(channelID);
    }

}
