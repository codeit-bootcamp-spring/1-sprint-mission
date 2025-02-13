package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.time.Instant;
import com.sprint.mission.discodeit.service.ReadStatusService;

import java.util.*;

@Service("basicChannelService")
@Primary
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Qualifier("basicReadStatusService")
    private final ReadStatusService readStatusService;

    @Override
    public void createPublicChannel(ChannelCreateDTO channelCreateDTO) {
        Channel channel = new Channel(
                UUID.randomUUID(),
                channelCreateDTO.getName(),
                channelCreateDTO.getDescription(),
                channelCreateDTO.getCreatorId(),
                false
        );
        channelRepository.save(channel);
    }

    @Override
    public void createPrivateChannel(UUID creatorId, List<UUID> memberIds) {  // ✅ PRIVATE 채널 생성
        Channel channel = new Channel(UUID.randomUUID(), null, null, creatorId, true);
        channel.getMembers().addAll(memberIds);
        channelRepository.save(channel);

        // ✅ 모든 멤버에 대해 ReadStatus 생성
        Instant now = Instant.now();
        for (UUID memberId : memberIds) {
            readStatusService.markAsRead(memberId, channel.getId(), now);
        }
    }

    @Override
    public List<ChannelDTO> readAll() {
        return channelRepository.findAll().stream()
                .map(channel -> new ChannelDTO(
                        channel.getId(),
                        channel.getName(),
                        channel.getDescription(),
                        channel.getCreatorId(),
                        channel.isPublic(),
                        channel.getCreatedAt(),
                        channel.getMembers()
                ))
                .toList();
    }

    @Override
    public Optional<ChannelDTO> read(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(channel -> new ChannelDTO(
                        channel.getId(),
                        channel.getName(),
                        channel.getDescription(),
                        channel.getCreatorId(),
                        channel.isPublic(),
                        channel.getCreatedAt(),
                        channel.getMembers()
                ));
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.deleteById(channelId);
    }
}

