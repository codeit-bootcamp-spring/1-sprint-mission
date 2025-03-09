package com.sprint.mission.service.jcf.main;

import com.sprint.mission.aop.notUsedAOP.annotation.TraceAnnotation;
import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.jcf.addOn.BinaryService;

import java.time.Instant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryService binaryService;

    @Override
    public Message create(MessageDtoForCreate responseDto, List<BinaryContentDto> binaryContentDtoList) {

        User author = userRepository.findById(responseDto.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));

        Channel writtenPlace = channelRepository.findById(responseDto.channelId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));

        Message createdMessage = new Message(writtenPlace, author, responseDto.content());

        log.info("attachmentsDto: {}", binaryContentDtoList);
        if (!binaryContentDtoList.isEmpty()) {
            for (BinaryContentDto bcd : binaryContentDtoList) {
                BinaryContent createdBinaryContent = binaryService.create(bcd);
                createdMessage.addAttachment(createdBinaryContent);
            }
        }
        //log.info("createdMessage 채널 : {}", createdMessage.getChannelId());
        //channelRepository.save(writtenChannel);
        return messageRepository.save(createdMessage);
    }

    @Override
    public void update(UUID messageId, MessageDtoForUpdate updateDto) {
        Message updatingMessage = this.findById(messageId);
        updatingMessage.update(updateDto.newContent());
        messageRepository.save(updatingMessage);
    }


    @Override
    public Message findById(UUID channelId) {
        return messageRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (channelRepository.existsById(channelId)) {
            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
        }
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public void delete(UUID messageId) {
        //Message deletingMessage = this.findById(messageId);
        // BinaryContent랑 cascade Remove관계라
        if (!messageRepository.existsById(messageId)) {
            throw new CustomException(ErrorCode.NO_SUCH_MESSAGE);
        } else {
            messageRepository.deleteById(messageId);
        }
    }


    @Override
    public void deleteAllByChannelId(UUID channelId) {
        messageRepository.deleteAllByChannelId(channelId);
    }
}
