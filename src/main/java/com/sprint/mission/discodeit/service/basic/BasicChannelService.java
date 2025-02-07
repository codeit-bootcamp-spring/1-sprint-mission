package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
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
    public Channel createPublicChannel(PublicChannelCreateRequest request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new IllegalArgumentException("User not found");
        }
        Channel channel=new Channel(PUBLIC,request.getChannelName(),request.getDescription(),request.getUserId());
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new IllegalArgumentException("User not found");
        }
        Channel channel=new Channel(PRIVATE,null,null,request.getUserId());
        Channel savedChannel=channelRepository.save(channel);

        readStatusService.addMembersToChannel(savedChannel.getId(),request.getMembers());

        return channelRepository.save(channel);
    }

    @Override
    public Optional<ChannelResponseDto> getChannelById(UUID id) {
        return channelRepository.getChannelById(id).map(channel -> {
            Optional<Instant> lastMessageTime=messageRepository.findLastMessageTimeByChannelId(id);
            List<UUID> members=channel.getType()== PRIVATE ? readStatusRepository.findMembersByChannelId(id) : Collections.emptyList();
            return new ChannelResponseDto(channel, lastMessageTime.orElse(null),members);
        });
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.getAllChannels();
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        return channelRepository.getAllChannels().stream()
                .filter(channel ->
                        channel.getType() == PUBLIC ||
                                readStatusRepository.isUserMemberOfChannel(userId, channel.getId()) // PRIVATE 채널 필터링
                )
                .map(channel -> {
                    // 해당 채널의 가장 최근 메시지 시간 조회
                    Optional<Instant> lastMessageTime = messageRepository.findLastMessageTimeByChannelId(channel.getId());

                    // PRIVATE 채널인 경우 참여한 User ID 정보 조회
                    List<UUID> members = channel.getType() == PRIVATE
                            ? readStatusRepository.findMembersByChannelId(channel.getId())
                            : Collections.emptyList();

                    // DTO에 포함하여 반환
                    return new ChannelResponseDto(channel, lastMessageTime.orElse(null), members);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Channel updateChannel(ChannelUpdateRequest request) {
        Channel channel = channelRepository.getChannelById(request.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));

        if (channel.getType() == PRIVATE) {
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
}
