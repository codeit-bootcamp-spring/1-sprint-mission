package com.sprint.mission.service.jcf.main;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFMessageRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.dto.request.BinaryMessageContentDto;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.jcf.addOn.BinaryMessageService;
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
    private final BinaryMessageService binaryMessageService;


    @Override
    public void create(MessageDtoForCreate responseDto) {
        Channel writtenChannel = channelRepository.findById(responseDto.getChannelId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));
        User writer = userRepository.findById(responseDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));

        // 첨부파일 미포함 메시지
        Message createdMessage = Message.createMessage(writtenChannel, writer, responseDto.getContent());

        // 첨부파일 적용
        List<byte[]> bmc = responseDto.getAttachments();
        if (!bmc.isEmpty()) {
            createdMessage.setAttachments(bmc);
            binaryMessageService.create(new BinaryMessageContentDto(createdMessage.getId(), bmc));
        }
        writtenChannel.updateLastMessageTime();
        channelRepository.save(writtenChannel);

        messageRepository.save(createdMessage);
    }

    @Override
    public void update(MessageDtoForUpdate updateDto) {
        Message updatingMessage = messageRepository.findById(updateDto.getMessageId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));

        updatingMessage.setContent(updateDto.getChangeContent());
        // 메시지 binary data는 수정 불가
        // 수정해도 채널의 lastMessageTime은 불변?
        messageRepository.save(updatingMessage);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return channelRepository.findById(channelId).map(messageRepository::findAllByChannel)
                .orElseGet(ArrayList::new);
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
