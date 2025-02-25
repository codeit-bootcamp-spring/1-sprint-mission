package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        UserResponseDto user = userService.findById(createMessageDto.userId());
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        ChannelResponseDto channel = channelService.findById(createMessageDto.channelId());
        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }

        if (channel.channelType() == ChannelType.PRIVATE && !channelService.isUserInChannel(channel.id(), user.id())) {
            System.out.println("User with id " + user.id() + " not found in this channel.");
            throw new CustomException(ErrorCode.USER_NOT_IN_CHANNEL);
        }

        Message message = new Message(createMessageDto.userId(), createMessageDto.content(), createMessageDto.channelId());
        Message saved = messageRepository.save(message);

        return MessageResponseDto.from(saved);

    }

    public MessageResponseDto create(CreateMessageDto createMessageDto, List<MultipartFile> files) throws CustomException {
        MessageResponseDto message = create(createMessageDto);

        if (files == null || files.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }
        List<String> binaryContentIds = new ArrayList<>();
        for (MultipartFile file : files) {
            ResponseBinaryContentDto responseBinaryContentDto = binaryContentService.create(file);
            binaryContentIds.add(responseBinaryContentDto.id());
        }
        return updateMessage(message.id(), new UpdateMessageDto(createMessageDto.userId(), message.content(), message.createdAt(), binaryContentIds));
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
        UserResponseDto byId = userService.findById(senderId);
        if (byId == null) {
            //todo - 고민: 메세지를 검색할때 유저 아이디가 없다고 에러를 출력해야할까?
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return messageRepository.findAll().stream().filter(m -> m.getSenderId().equals(senderId)).map(MessageResponseDto::from).toList();
    }

    @Override
    public List<MessageResponseDto> findAllByCreatedAt(Instant createdAt) {
        return messageRepository.findAll().stream().filter(m -> m.getCreatedAt().equals(createdAt)).map(MessageResponseDto::from).toList();
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(String channelId) {
        ChannelResponseDto byId = channelService.findById(channelId);
        if (byId == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }

        return messageRepository.findAll().stream().filter(m -> m.getChannelId().equals(channelId)).map(MessageResponseDto::from).toList();
    }

    @Override
    public MessageResponseDto updateMessage(String messageId, UpdateMessageDto updateMessageDto) throws CustomException {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }
        if (updateMessageDto.newContent().isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "Content is empty");
        }
        if (!message.getSenderId().equals(updateMessageDto.userId())) {
            throw new CustomException(ErrorCode.MESSAGE_OWNER_NOT_MATCH);
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
    public boolean delete(String messageId, String userId) throws CustomException {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }
        if (!message.getSenderId().equals(userId)) {
            throw new CustomException(ErrorCode.MESSAGE_OWNER_NOT_MATCH);
        }

        for (String imageId : message.getAttachmentImageIds()) {
            binaryContentService.deleteById(imageId);
        }

        return messageRepository.delete(messageId);
    }
}
