package com.sprint.mission.service.jcf.main;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFMessageService implements MessageService {

    private final JCFMessageRepository messageRepository;
    private final JCFChannelRepository channelRepository;
    private final JCFUserRepository userRepository;
    private final BinaryService binaryService;

    @Override
    public void create(MessageDtoForCreate responseDto, Optional<List<BinaryContentDto>> attachmentsDto) {
        UUID channelId = responseDto.getChannelId();
        UUID userId = responseDto.getUserId();
        if(!channelRepository.existsById(channelId)){
            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
        }

        if(!userRepository.existsById(userId)){
            throw new CustomException(ErrorCode.NO_SUCH_USER);
        }

        Message createdMessage = Message.createMessage(channelId, userId, responseDto.getContent());

        if (!attachmentsDto.isEmpty()) {
            List<BinaryContentDto> binaryContentDtoList = attachmentsDto.get();
            for (BinaryContentDto binaryContentDto : binaryContentDtoList) {
                BinaryContent createdBinaryContent = binaryService.create(binaryContentDto);
                createdMessage.getAttachmentIdList().add(createdBinaryContent.getId());
            }
        }
        //writtenChannel.updateLastMessageTime();
        //channelRepository.save(writtenChannel);
        messageRepository.save(createdMessage);
    }

    @Override
    public void update(UUID messageId, MessageDtoForUpdate updateDto) {
        binaryMessageService.delete(messageId);

        Message updatingMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));

        updatingMessage.setContent(updateDto.getContent());
        updatingMessage.setAttachments(updateDto.getBinaryContentList());
        binaryMessageService.create(new BinaryMessageContentDto(messageId, updateDto.getBinaryContentList()));

        // 수정해도 채널의 lastMessageTime은 불변?
        messageRepository.save(updatingMessage);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (channelRepository.existsById(channelId)){
            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
        }

        return messageRepository.findAllByChannel(channelId);
    }

    @Override
    public void delete(UUID messageId) {
        binaryMessageService.delete(messageId);
        if (messageRepository.existsById(messageId)) messageRepository.delete(messageId);
        else throw new CustomException(ErrorCode.NO_SUCH_MESSAGE);
    }

    @Override
    public Message findById(UUID channelId) {
        return messageRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));
    }
}
