package com.sprint.mission.discodeit.service.basic;



import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.dto.response.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService{
    private final MessageRepository repository;
    private final BinaryContentRepository binaryContentRepository;
    private final PageMapper pageMapper;

    @Override
    public MessageResponse create(MessageRequest messageRequest) {
//        Message message;
//        boolean isPrivateMessage = messageRequest.recipientId() != null;
//        boolean isChannelMessage = messageRequest.channelId() != null;
//
//        if (isPrivateMessage) {
//            // 1:1 메시지 생성
//            message = new Message(
//                    messageRequest.content(),
//                    messageRequest.senderId(),
//                    messageRequest.recipientId(),
//                    null
//            );
//        } else if (isChannelMessage) {
//            // 채널 메시지 생성
//            message = new Message(
//                    messageRequest.content(),
//                    messageRequest.senderId(),
//                    null,
//                    messageRequest.channelId()
//            );
//        } else {
//            throw new IllegalArgumentException("Either recipientId or channelId must be provided.");
//        }

        try{
            Message message = MessageMapper.INSTANCE.toEntity(messageRequest);
            repository.save(message);
            return MessageMapper.INSTANCE.toDto(message);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Either recipientId or channelId must be provided." + e);
        }
    }

    @Transactional
    @Override
    public MessageResponse messageCreate(MessageRequest request, MultipartFile[] files){
        try{
            Message message = MessageMapper.INSTANCE.toEntity(request);
            repository.save(message);


            for(MultipartFile file : files) {
                BinaryContent imageData = null;
                if(file != null && !file.isEmpty()){
                    imageData = new BinaryContent(message.getId(), file.getOriginalFilename(), file.getSize(), file.getContentType());

                    binaryContentRepository.save(imageData);

                }

            }
            return MessageMapper.INSTANCE.toDto(message);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Either recipientId or channelId must be provided." + e);
        }
    }

    @Override
    public MessageResponse readOne(UUID id) {
        Message message = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디입니다. : " + id));
        return MessageMapper.INSTANCE.toDto(message);
    }

    @Override
    public List<MessageResponse> readAll() {
        List<Message> messages = repository.findAll();
        List<MessageResponse> responses = messages.stream()
                            .map(message -> MessageMapper.INSTANCE.toDto(message))
                            .collect(Collectors.toList());
        return responses;
    }

//    @Transactional(readOnly = true)
//    public PageResponse<MessageResponse> getMessages(Pageable pageable) {
//        Page<Message> page = repository.findAllByOrderByCreatedAtDesc(pageable);
//        return pageMapper.toPageResponse(page.map(message -> MessageMapper.INSTANCE.toDto(message)));
//    }

    @Override
    public List<MessageResponse> channelMessageReadAll(UUID channelId) {
        List<Message> messages = repository.findAll();
        List<MessageResponse> responses = messages.stream()
                .filter(message -> message.getChannelId() !=null && message.getChannelId().equals(channelId))
                .map(message -> MessageMapper.INSTANCE.toDto(message))
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public MessageResponse update(UUID id, MessageRequest messageRequest) {
//        if (messageRequest.recipientId() != null){
//            Message modifiMessage = new Message(messageRequest.content(), messageRequest.senderId(), messageRequest.recipientId(), null);
//        }
//
//        Message modifiMessage = new Message(messageRequest.content(), messageRequest.senderId(), null, messageRequest.channelId());

        Message modifiMessage = MessageMapper.INSTANCE.toEntity(messageRequest);

        Message message = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디입니다. : " + id));

        message.setContent(modifiMessage.getContent());
//        message.setSenderId(modifiMessage.getSenderId());
//        message.setRecipientId(modifiMessage.getRecipientId());
//        message.setChannelId(modifiMessage.getChannelId());

        repository.save(message);

        return MessageMapper.INSTANCE.toDto(message);
    }

    @Override
    public boolean delete(UUID id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("해당 ID의 사용자가 존재하지 않습니다 : " + id);
        }

        repository.deleteById(id);
        return true;
    }

}
