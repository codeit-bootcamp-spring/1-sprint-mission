package com.sprint.mission.discodeit.service.basic;



import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.dto.response.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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

        log.debug("메시지 생성 시도 [파일 미포함] : request : {} ", messageRequest);
        try{
            Message message = MessageMapper.INSTANCE.toEntity(messageRequest);
            repository.save(message);

            log.info("메시지 생성 완료 - message : {} ", message);
            return MessageMapper.INSTANCE.toDto(message);
        }catch (IllegalArgumentException e){
            log.warn("메시지 생성 실패 - request : {} ", messageRequest);
            throw new IllegalArgumentException("Either recipientId or channelId must be provided." + e);
        }
    }

    @Transactional
    @Override
    public MessageResponse messageCreate(MessageRequest request, MultipartFile[] files){
        log.debug("메시지 생성 시도 [파일 포함] : request : {} ", request);
        try{
            Message message = MessageMapper.INSTANCE.toEntity(request);
            repository.save(message);
            log.info("메시지 저장 완료 - message : {} ", message);

            log.debug("메시지에 포함된 파일 저장 시도 : files : {} ", files);
            for(MultipartFile file : files) {
                BinaryContent imageData = null;
                if(file != null && !file.isEmpty()){
                    imageData = new BinaryContent(message.getId(), file.getOriginalFilename(), file.getSize(), file.getContentType());

                    binaryContentRepository.save(imageData);
                    log.info("파일 저장 완료 - imageData : {} ", imageData);
                }
            }
            log.info("저장된 파일 개수 : {} ", files.length);

            log.info("메시지 생성 완료 - message : {} , files : {} ", message, files);
            return MessageMapper.INSTANCE.toDto(message);
        }catch (IllegalArgumentException e){
            log.warn("메시지 생성 실패 - request : {} ", request);
            throw new IllegalArgumentException("Either recipientId or channelId must be provided." + e);
        }
    }

    @Override
    public MessageResponse readOne(UUID id) {
        log.debug("메시지 단건 조회 시도 : id : {} ", id);
        Message message = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("메시지 단건 조회 실패 - 저장되지 않았거나, 삭제된 id : {}", id);
                    return new MessageNotFoundException(id);
                });

        log.info("메시지 단건 조회 완료 - id : {} ", id);
        return MessageMapper.INSTANCE.toDto(message);
    }

    @Override
    public List<MessageResponse> readAll() {
        log.debug("메시지 전체 조회 시도");

        List<Message> messages = repository.findAll();
        log.debug("조회된 메시지 개수 : {}", messages.size());

        List<MessageResponse> responses = messages.stream()
                            .map(message -> MessageMapper.INSTANCE.toDto(message))
                            .collect(Collectors.toList());

        log.info("메시지 조회 완료 - 총 {}개", responses.size());
        return responses;
    }

//    @Transactional(readOnly = true)
//    public PageResponse<MessageResponse> getMessages(Pageable pageable) {
//        Page<Message> page = repository.findAllByOrderByCreatedAtDesc(pageable);
//        return pageMapper.toPageResponse(page.map(message -> MessageMapper.INSTANCE.toDto(message)));
//    }

    @Override
    public List<MessageResponse> channelMessageReadAll(UUID channelId) {
        log.debug("특정 채널 메시지 전체 조회 : channelId : {} ", channelId);

        List<Message> messages = repository.findAll();
        log.debug("조회된 메시지 개수 : {}", messages.size());

        List<MessageResponse> responses = messages.stream()
                .filter(message -> message.getChannelId() !=null && message.getChannelId().equals(channelId))
                .map(message -> MessageMapper.INSTANCE.toDto(message))
                .collect(Collectors.toList());

        log.info("메시지 조회 완료 - 총 {}개 ", responses.size());
        return responses;
    }

    @Override
    public MessageResponse update(UUID id, MessageRequest messageRequest) {
//        if (messageRequest.recipientId() != null){
//            Message modifiMessage = new Message(messageRequest.content(), messageRequest.senderId(), messageRequest.recipientId(), null);
//        }
//
//        Message modifiMessage = new Message(messageRequest.content(), messageRequest.senderId(), null, messageRequest.channelId());

        log.debug("메시지 수정 요청 - id: {}, updateMessage: {}", id, messageRequest);

        Message modifiMessage = MessageMapper.INSTANCE.toEntity(messageRequest);

        Message message = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("메시지 조회 실패 - 저장되지 않았거나, 삭제된 id: {}", id);
                    return new MessageNotFoundException(id);
                });

        message.setContent(modifiMessage.getContent());
//        message.setSenderId(modifiMessage.getSenderId());
//        message.setRecipientId(modifiMessage.getRecipientId());
//        message.setChannelId(modifiMessage.getChannelId());

        repository.save(message);

        log.info("메시지 수정 성공 - id: {}", id);
        return MessageMapper.INSTANCE.toDto(message);
    }

    @Override
    public boolean delete(UUID id) {
        log.debug("메시지 삭제 요청 - id: {}", id);

        if(!repository.existsById(id)){
            log.warn("메시지 삭제 실패 - 없거나 삭제된 ID : {}", id);
            throw new MessageNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("메시지 삭제 완료 - id : {}", id);
        return true;
    }

}
