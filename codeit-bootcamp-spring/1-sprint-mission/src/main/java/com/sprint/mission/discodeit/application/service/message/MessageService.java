package com.sprint.mission.discodeit.application.service.message;

import com.sprint.mission.discodeit.application.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.application.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.application.service.channel.ChannelService;
import com.sprint.mission.discodeit.application.service.user.UserService;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.repository.message.interfaces.MessageRepository;

public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    public MessageService(
            MessageRepository messageRepository,
            ChannelService channelService,
            UserService userService
    ) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
        this.userService = userService;
    }

    public MessageResponseDto createMessage(CreateMessageRequestDto requestDto) {
        User sender = userService.findOneByIdOrThrow(requestDto.userId());
        Channel destinationChannel = channelService.findOneByIdOrThrow(requestDto.destinationChannelId());
        Message createMessage = messageRepository.save(new Message(sender, destinationChannel, requestDto.content()));
        return MessageResponseDto.from(createMessage);
    }
}
