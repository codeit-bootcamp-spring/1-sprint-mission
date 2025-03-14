package com.sprint.mission.service.jcf.main;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.MessageMapper;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.BinaryContentStorage;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.jcf.addOn.BinaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryService binaryService;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;


    @Override
    public Message create(MessageDtoForCreate responseDto, List<BinaryContentDtoForCreate> binaryContentDtoForCreateList) {

        User author = userRepository.findById(responseDto.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));

        Channel writtenPlace = channelRepository.findById(responseDto.channelId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));

        Message createdMessage = messageMapper.toEntity(writtenPlace, author, responseDto.content());
        //Message createdMessage = new Message(writtenPlace, author, responseDto.content());

        log.info("attachmentsDto: {}", binaryContentDtoForCreateList);
        if (!binaryContentDtoForCreateList.isEmpty()) {
            for (BinaryContentDtoForCreate bcd : binaryContentDtoForCreateList) {
                BinaryContent createdBinaryContent = binaryService.create(bcd);
                log.info("메시지의 생성된 BinaryContent: {}", createdBinaryContent);
                binaryContentStorage.put(createdBinaryContent.getId(), bcd.bytes());
                createdMessage.addAttachment(createdBinaryContent);
            }
        }
        //log.info("createdMessage 채널 : {}", createdMessage.getChannelId());
        //channelRepository.save(writtenChannel);
        return messageRepository.save(createdMessage);
    }

    @Override
    public Message update(UUID messageId, MessageDtoForUpdate updateDto) {
        Message updatingMessage = this.findById(messageId);
        updatingMessage.update(updateDto.content());
        return updatingMessage;
    }


    @Override
    public Message findById(UUID channelId) {
        return messageRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
        }
        return messageRepository.findAllByChannel_Id(channelId);
    }
//
//        Page<Message> paging = (Page<Message>) pageable;
//        do {
//            Slice<Message> sliceMessage = messageRepository.findAllByChannelId(channelId, paging);
//            //paging = sliceMessage.getPageable();
//        } while (paging.hasNext());
//
//        Slice<Message> sliceMessage = messageRepository.findAllByChannelId(channelId, pageable);
//        sliceMessage.getNumberOfElements();



//    @Override
//    public List<Message> findAllByChannelId(UUID channelId) {
//        if (channelRepository.existsById(channelId)) {
//            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
//        }
//        return messageRepository.findAllByChannelId(channelId);
//    }

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
        messageRepository.deleteAllByChannel_Id(channelId);
    }
}
