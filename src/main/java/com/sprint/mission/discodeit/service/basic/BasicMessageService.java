package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;
    private final BinaryContentService binaryContentService;

    @Override
    public MessageResponseDto create(CreateMessageDto createMessageDto) throws CustomException {
        if (createMessageDto.content() == null || createMessageDto.content().isEmpty()) {
            System.out.println("MessageResponseDto content is empty for User: " + createMessageDto.userId() + " Channel: " + createMessageDto.channelId());
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }

        try {
            //수정해야함
            UserResponseDto user = userService.findById(createMessageDto.userId());
            ChannelResponseDto channel = channelService.findById(createMessageDto.channelId());
            if (!channelService.isUserInChannel(channel.id(), user.id())) {
                System.out.println("User with id " + user.id() + " not found in this channel.");
                throw new CustomException(ErrorCode.USER_NOT_IN_CHANNEL);
            }
            Message message = new Message(createMessageDto.userId(), createMessageDto.content(), createMessageDto.channelId());
            Message saved = messageRepository.save(message);

            return MessageResponseDto.from(saved);
        } catch (CustomException e) {
            System.out.println("Failed to create message. User: " + createMessageDto.userId() + " Channel: " + createMessageDto.channelId() + " Content: " + createMessageDto.content());
            if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
                throw e;
            } else if (e.getErrorCode() == ErrorCode.CHANNEL_NOT_FOUND) {
                throw e;
            }
            throw new CustomException(e.getErrorCode(), "Create message failed");
        }

    }

    public MessageResponseDto create(CreateMessageDto createMessageDto, List<CreateBinaryContentDto> binaryContents) throws CustomException {
        MessageResponseDto message = create(createMessageDto);

        if (binaryContents == null || binaryContents.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }
        List<String> binaryContentIds = new ArrayList<>();
        for (CreateBinaryContentDto binaryContentDto : binaryContents) {
            ResponseBinaryContentDto responseBinaryContentDto = binaryContentService.create(binaryContentDto);
            binaryContentIds.add(responseBinaryContentDto.id());
        }
        return updateMessage(message.id(), new UpdateMessageDto(message.content(), message.createdAt(), binaryContentIds));
    }

    @Override
    public List<MessageResponseDto> findAll() {
        return messageRepository.findAll().stream().map(MessageResponseDto::from).toList();
    }

    @Override
    public MessageResponseDto findById(String messageId) {
        return MessageResponseDto.from(messageRepository.findById(messageId));
    }

    @Override
    public List<MessageResponseDto> findAllContainsContent(String content) {
        return messageRepository.findAll().stream().filter(m -> m.getContent().contains(content)).map(MessageResponseDto::from).toList();
    }

    @Override
    public List<MessageResponseDto> findAllBySenderId(String senderId) {
        //여기 고민해보자
        return messageRepository.findAll().stream().filter(m -> m.getSenderId().equals(senderId)).map(MessageResponseDto::from).toList();
    }

    @Override
    public List<MessageResponseDto> findAllByCreatedAt(Instant createdAt) {
        return messageRepository.findAll().stream().filter(m -> m.getCreatedAt().equals(createdAt)).map(MessageResponseDto::from).toList();
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(String channelId) {
        return messageRepository.findAll().stream().filter(m -> m.getChannelId().equals(channelId)).map(MessageResponseDto::from).toList();
    }

    @Override
    public MessageResponseDto updateMessage(String messageId, UpdateMessageDto updateMessageDto) throws CustomException {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        } else if (updateMessageDto.newContent().isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }

        if (!message.getContent().equals(updateMessageDto.newContent())) {
            message.setContent(updateMessageDto.newContent());
            message.setUpdatedAt(updateMessageDto.updatedAt());
        }
        //todo
        //메세지의 이미지를 삭제하거나 추가하는 경우
        //if(!updateMessageDto.binaryContentIds().isEmpty()) {
            //여기 수정해야겠다
            //이미지자체를 받아와서
            //이미있으면 그냥 넘어가고
            //아니라면 추가해야함
            //message.addImages(updateMessageDto.binaryContentIds());
        //}

        return MessageResponseDto.from(messageRepository.save(message));
    }

    @Override
    public boolean delete(String messageId) throws CustomException {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        for (String imageId : message.getAttachmentImageIds()) {
            binaryContentService.deleteById(imageId);
        }

        return messageRepository.delete(messageId);
    }
}
