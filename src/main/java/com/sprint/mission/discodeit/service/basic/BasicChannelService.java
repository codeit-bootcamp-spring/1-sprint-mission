package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ValidateChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ValidateChannel validateChannel;

    @Override
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
        validateChannel.validChannelType(request.channelType());

        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        channelRepository.save(channel);
        for (UUID userId : request.participants()) {
            channel.addParticipant(userId);
            readStatusRepository.save(new ReadStatus(userId, channel.getId()));
        }
        return changeToDto(channel);
    }

    @Override
    public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
        validateChannel.validatePublicChannel(request.name(), request.description(), request.channelType());

        Channel channel = new Channel(request.name(), request.description(), ChannelType.PUBLIC);
        channelRepository.save(channel); // 모든 사용자 조회
        for (UUID userId : request.participants()) {
            channel.addParticipant(userId);
            readStatusRepository.save(new ReadStatus(userId, channel.getId()));
        }
        return changeToDto(channel);
    }

    @Override
    public ChannelDto findById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found."));
        return changeToDto(channel);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        return channels.stream()
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC || channel.getParticipants().contains(userId))
                .map(BasicChannelService::changeToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChannelDto update(ChannelUpdateRequest request) {
        validateChannel.validatePublicChannelType(request.channelType());
        validateChannel.validatePublicChannel(request.name(), request.description(), request.channelType());

        Channel channel = channelRepository.findById(request.ChannelId())
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found."));
        for (UUID userId : channel.getParticipants()){
            if (!request.participants().contains(userId)){
                channel.removeParticipant(userId);
                ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(userId, request.ChannelId())
                                .orElseThrow(() -> new ResourceNotFoundException("ReadStatus not found."));
                readStatusRepository.delete(readStatus.getId());
            }
        }
        readStatusRepository.deleteByChannelId(request.ChannelId());
        for (UUID userId : request.participants()) {
            if (!channel.getParticipants().contains(userId)){
                channel.addParticipant(userId);
                readStatusRepository.save(new ReadStatus(userId, channel.getId()));
            }
        }
        channel.update(request.name(), request.description(), request.channelType(), request.participants());
        return changeToDto(channel);
    }

    @Override
    public void delete(UUID channelId) {
        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.deleteByChannelId(channelId);
    }

    private static ChannelDto changeToDto(Channel channel) {
        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getChannelType(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                new ArrayList<>(channel.getParticipants())
        );
    }
}
