package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;
    private final BinaryContentService binaryContentService;

    @Override
    public Message create(CreateMessageDto createMessageDto) throws CustomException {
        if (createMessageDto.content() == null || createMessageDto.content().isEmpty()) {
            System.out.println("Message content is empty for User: " + createMessageDto.userId() + " Channel: " + createMessageDto.channelId());
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
            Message message = new Message(user.id(), createMessageDto.content(),channel.id());
            return messageRepository.save(message);
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

    public Message create(CreateMessageDto createMessageDto, List<CreateBinaryContentDto> binaryContents) throws CustomException {
        Message message = create(createMessageDto);

        if(binaryContents == null || binaryContents.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }
        for(CreateBinaryContentDto binaryContentDto : binaryContents) {
            ResponseBinaryContentDto  responseBinaryContentDto= binaryContentService.create(binaryContentDto);
            message.addImages(responseBinaryContentDto.id());
        }
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message findById(String messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> findAllContainsContent(String content) {
        return messageRepository.findAll().stream().filter(m -> m.getContent().contains(content)).toList();
    }

    @Override
    public List<Message> findAllBySenderId(String senderId) {
        //여기 고민해보자
        return messageRepository.findAll().stream().filter(m -> m.getSenderId().equals(senderId)).toList();
    }

    @Override
    public List<Message> findAllByCreatedAt(Instant createdAt) {
        return messageRepository.findAll().stream().filter(m -> m.getCreatedAt().equals( createdAt)).toList();
    }

    @Override
    public List<Message> findAllByChannelId(String channelId) {
        return messageRepository.findAll().stream().filter(m -> m.getChannelId().equals(channelId)).toList();
    }

    @Override
    public Message updateMessage(String messageId, UpdateMessageDto updateMessageDto) throws CustomException {
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
        return messageRepository.save(message);
    }

    @Override
    public boolean delete(String messageId) throws CustomException {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        for(String imageId : message.getAttachmentImageIds()){
            binaryContentService.deleteById(imageId);
        }

        return messageRepository.delete(messageId);
    }
}
