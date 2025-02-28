package com.sprint.mission.service.jcf.main;

import com.sprint.mission.aop.notUsedAOP.annotation.TraceAnnotation;
import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFMessageRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.jcf.addOn.BinaryService;

import java.time.Instant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFMessageService implements MessageService {

    // 가상스레드
    private final ExecutorService ves;

    private final JCFMessageRepository messageRepository;
    private final JCFChannelRepository channelRepository;
    private final JCFUserRepository userRepository;
    private final BinaryService binaryService;

    @Override
    public Message create(MessageDtoForCreate responseDto, Optional<List<BinaryContentDto>> attachmentsDto) {
        UUID userId = responseDto.userId();
        UUID channelId = responseDto.channelId();
        Future<?> isExistUserF = ves.submit(() -> {
            if (!userRepository.existsById(channelId)) throw new CustomException(ErrorCode.NO_SUCH_USER);});
        Future<?> isExistChannelF = ves.submit(() -> {
            if (!channelRepository.existsById(channelId)) throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);});
        try {
            isExistUserF.get();
            isExistChannelF.get();
        } catch (ExecutionException e) {
            throw e.getCause() instanceof CustomException
                    ? (CustomException) e.getCause()
                    : new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Message createdMessage = responseDto.toEntity();

        List<BinaryContentDto> bcdList = attachmentsDto.orElse(Collections.emptyList());
        log.info("attachmentsDto: {}", bcdList);
        if (!bcdList.isEmpty()) {
            for (BinaryContentDto bcd : bcdList) {
                BinaryContent createdBinaryContent = binaryService.create(bcd);
                createdMessage.getAttachmentIdList().add(createdBinaryContent.getId());
            }
        }
        log.info("createdMessage 채널 : {}", createdMessage.getChannelId());
        //writtenChannel.updateLastMessageTime();
        //channelRepository.save(writtenChannel);
        return messageRepository.save(createdMessage);
    }

    @Override
    public void update(UUID messageId, MessageDtoForUpdate updateDto) {
        Message updatingMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));
        updatingMessage.setContent(updateDto.newContent());
        updatingMessage.setUpdateAt(Instant.now());
        messageRepository.save(updatingMessage);
    }


    @Override
    public Message findById(UUID channelId) {
        return messageRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
//        if (channelRepository.existsById(channelId)){
//            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
//        }
        return messageRepository.findAllByChannel(channelId);
    }

    @Override
    public void delete(UUID messageId) {
        Message deletingMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));

        Future<?> deleteBinaryF = ves.submit(() -> {
            deletingMessage.getAttachmentIdList()
                    .forEach(binaryService::deleteById);
        });
        ves.submit(() -> messageRepository.delete(deletingMessage.getId()));
        try {
            deleteBinaryF.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw e.getCause() instanceof CustomException
                    ? (CustomException) e.getCause()
                    : new RuntimeException(e);
        }
    }


    @Override
    public void deleteAllByChannelId(UUID channelId) {
        messageRepository.deleteAllByChannelId(channelId);
    }
}
