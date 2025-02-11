package com.sprint.mission.discodeit.application.service.message;

import com.sprint.mission.discodeit.application.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.application.dto.message.DeleteMessageRequestDto;
import com.sprint.mission.discodeit.application.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.application.dto.message.UpdateMessageContentRequestDto;
import com.sprint.mission.discodeit.application.service.interfaces.ChannelService;
import com.sprint.mission.discodeit.application.service.interfaces.MessageService;
import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.application.service.readstatus.ReadStatusService;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.domain.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.repository.message.interfaces.MessageRepository;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;
    private final ReadStatusService readStatusService;

    public JCFMessageService(
            MessageRepository messageRepository,
            ChannelService channelService,
            UserService userService,
            ReadStatusService readStatusService
    ) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
        this.userService = userService;
        this.readStatusService = readStatusService;
    }

    @Override
    public MessageResponseDto createMessage(CreateMessageRequestDto requestDto) {
        User sender = userService.findOneByUserIdOrThrow(requestDto.userId());
        Channel destinationChannel = channelService.findOneByChannelIdOrThrow(requestDto.destinationChannelId());
        Message createMessage = messageRepository.save(new Message(sender, destinationChannel, requestDto.content()));
        return MessageResponseDto.from(createMessage);
    }

    @Override
    public void updateMessage(UUID userId, UpdateMessageContentRequestDto requestDto) {
        User foundUser = userService.findOneByUserIdOrThrow(userId);
        Message foundMessage = findOneByMessageIdOrThrow(requestDto.messageId());
        throwIsNotSender(foundUser, foundMessage);
        foundMessage.updateContent(requestDto.content());
        readStatusService.updateLastReadTime(foundUser, foundMessage.getDestinationChannel());
        messageRepository.save(foundMessage);
    }

    @Override
    public void deleteMessage(UUID userId, DeleteMessageRequestDto requestDto) {
        User foundUser = userService.findOneByUserIdOrThrow(userId);
        Message foundMessage = findOneByMessageIdOrThrow(requestDto.messageId());
        throwIsNotSender(foundUser, foundMessage);
        messageRepository.deleteById(foundMessage.getId());
    }

    @Override
    public Message findOneByMessageIdOrThrow(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(ErrorCode.NOT_FOUND));
    }

    private void throwIsNotSender(User foundUser, Message foundMessage) {
        if (!foundMessage.isSender(foundUser)) {
            throw new IllegalArgumentException();
        }
    }
}
