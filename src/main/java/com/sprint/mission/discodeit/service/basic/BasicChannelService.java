package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.ChannelUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;


@Service("basicChannelService")
@Primary
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public ChannelDTO createChannel(ChannelCreateDTO channelCreateDTO) {
        Channel channel = new Channel(
                UUID.randomUUID(),
                channelCreateDTO.getName(),
                channelCreateDTO.getDescription(),
                channelCreateDTO.getCreatorId(),
                channelCreateDTO.isPrivate(),
                Instant.now(),
                new ArrayList<>()
        );
        channelRepository.save(channel);
        return new ChannelDTO(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getCreatorId(),
                channel.isPrivate(),
                channel.getCreatedAt(),
                channel.getMembers()
        );
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
    public void update(UUID channelId, ChannelUpdateDTO channelUpdateDTO) {
        channelRepository.findById(channelId).ifPresent(channel -> {
            if (channel.isPrivate()) {
                throw new IllegalArgumentException("Private channels cannot be updated.");
            }
            channel.setName(channelUpdateDTO.getName());
            channel.setDescription(channelUpdateDTO.getDescription());
            channelRepository.save(channel);
        });
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.deleteById(channelId);
    }

    @Override
    public List<ChannelDTO> getChannelsForUser(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel -> channel.isPublic() || channel.getMembers().contains(userId))
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
}
