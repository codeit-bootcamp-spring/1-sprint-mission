package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    private final UserMapper userMapper;
    private final BinaryContentMapper binaryContentMapper;

    public MessageResponseDto toResponseDto(Message message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .channelId(message.getChannel().getId())
                .author(userMapper.toResponseDto(message.getAuthor()))
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .attachments(message.getAttachments().isEmpty() ? null :
                        message.getAttachments().stream()
                                .map(messageAttachment -> binaryContentMapper.toResponseDto(messageAttachment.getAttachment()))
                                .collect(Collectors.toList())
                )
                .build();
    }

    public Message toEntity(MessageRequestDto messageRequestDto) {
        User author = userRepository.findById(messageRequestDto.authorId()).orElseThrow(() -> new UserNotFoundException(messageRequestDto.authorId()));
        Channel channel = channelRepository.findById(messageRequestDto.channelId()).orElseThrow(() -> new ChannelNotFoundException(messageRequestDto.channelId()));
        return new Message(messageRequestDto.content(), channel, author);
    }
}
