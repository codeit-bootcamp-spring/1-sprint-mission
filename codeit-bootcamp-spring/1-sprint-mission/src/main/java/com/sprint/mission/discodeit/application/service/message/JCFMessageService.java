package com.sprint.mission.discodeit.application.service.message;

import com.sprint.mission.discodeit.application.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.application.dto.message.DeleteMessageRequestDto;
import com.sprint.mission.discodeit.application.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.application.dto.message.UpdateMessageContentRequestDto;
import com.sprint.mission.discodeit.application.service.interfaces.ChannelService;
import com.sprint.mission.discodeit.application.service.interfaces.MessageService;
import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.domain.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.repository.message.interfaces.MessageRepository;
import java.time.LocalDateTime;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    public JCFMessageService(
            MessageRepository messageRepository,
            ChannelService channelService,
            UserService userService
    ) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public MessageResponseDto createMessage(CreateMessageRequestDto requestDto) {
        User sender = userService.findOneByIdOrThrow(requestDto.userId());
        Channel destinationChannel = channelService.findOneByIdOrThrow(requestDto.destinationChannelId());
        Message createMessage = messageRepository.save(new Message(sender, destinationChannel, requestDto.content()));
        return MessageResponseDto.from(createMessage);
    }

    @Override
    public void updateMessage(UUID userId, UpdateMessageContentRequestDto requestDto) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        Message foundMessage = findOneByIdOrThrow(requestDto.messageId());
        throwIsNotSender(foundUser, foundMessage);
        foundMessage.updateContent(requestDto.content());
        messageRepository.save(foundMessage);
    }

    @Override
    public void deleteMessage(UUID userId, DeleteMessageRequestDto requestDto) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        Message foundMessage = findOneByIdOrThrow(requestDto.messageId());
        throwIsNotSender(foundUser, foundMessage);
        messageRepository.deleteById(foundMessage.getId());
    }

    @Override
    public Message findOneByIdOrThrow(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Override
    public LocalDateTime getLastMessageTime(UUID channelId) {
        return null;
    }

    private void throwIsNotSender(User foundUser, Message foundMessage) {
        if (!foundMessage.isSender(foundUser)) {
            throw new IllegalArgumentException();
        }
    }
}
