package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.ChannelService;
import com.sprint.mission.discodeit.service.Interface.MessageService;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.entity.ChannelType.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {


    @Autowired
    private final ChannelRepository channelRepository;
    @Autowired
    private final MessageRepository messageRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ReadStatusRepository readStatusRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ReadStatusService readStatusService;


    @Override
    public ChannelDto createPublicChannel(PublicChannelCreateRequestDto request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new IllegalArgumentException("User not found");
        }
        Channel channel=new Channel(PUBLIC,request.getChannelName(),request.getDescription(),request.getUserId());
        return toDto(channelRepository.save(channel));
    }

    @Override
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequestDto request) {
        if (request.getMembers().stream().anyMatch(userId -> !userRepository.existsById(userId))) {
            throw new IllegalArgumentException("User not found");
        }
        Channel channel=new Channel(PRIVATE,null,null,request.getUserId());
        Channel savedChannel=channelRepository.save(channel);

        request.getMembers().stream()
                .map(userId->new CreateReadStatusRequestDto(userId,savedChannel.getId()))
                .forEach(readStatusService::create);

        return toDto(savedChannel);
    }

    @Override
    public ChannelDto getChannelById(UUID id) {
       return channelRepository.getChannelById(id)
               .map(this::toDto)
               .orElseThrow(()->new NoSuchElementException("Channel whith id "+" not found"));
    }

    @Override
    public List<ChannelDto> getAllChannels() {
        List<Channel> channels = channelRepository.getAllChannels();
        return channels.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        return channelRepository.getAllChannels().stream()
                .filter(channel ->
                        channel.getType() == PUBLIC ||
                                readStatusRepository.isUserMemberOfChannel(userId, channel.getId()) // PRIVATE 채널 필터링
                )
                .map(channel -> {
                    // 해당 채널의 가장 최근 메시지 시간 조회
                    Instant lastMessageTime = messageRepository.findLastMessageTimeByChannelId(channel.getId())
                            .orElse(Instant.MIN);

                    // PRIVATE 채널인 경우 참여한 User ID 정보 조회
                    List<UUID> members = channel.getType() == PRIVATE
                            ? readStatusRepository.findMembersByChannelId(channel.getId())
                            : Collections.emptyList();

                    // DTO에 포함하여 반환
                    return new ChannelDto(channel.getId(),
                            channel.getChannelName(),
                            channel.getType(),
                            channel.getDescription(),
                            lastMessageTime,
                            members);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Channel updateChannel(UUID id,ChannelUpdateRequestDto request) {
        Channel channel = channelRepository.getChannelById(request.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));

        if (channel.getType().equals(PRIVATE)) {
            throw new IllegalStateException("Cannot update private channels");
        }
        channel.update(request.getNewChannelName(), request.getNewDescription());
        return channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        if (!channelRepository.existsById(id)) {
            throw new NoSuchElementException("Channel not found");
        }
        messageService.deleteByChannelId(id);
        readStatusService.deleteByChannelId(id);
        channelRepository.deleteChannel(id);
    }
    private ChannelDto toDto(Channel channel) {
        Instant lastMessageAt = messageService.findAllByChannelId(channel.getId())
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);

        List<UUID> participantIds = new ArrayList<>();
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            readStatusService.findAllByChannelId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUserId)
                    .forEach(participantIds::add);
        }

        return new ChannelDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getType(),
                channel.getDescription(),
                lastMessageAt,
                participantIds
        );
    }
}
