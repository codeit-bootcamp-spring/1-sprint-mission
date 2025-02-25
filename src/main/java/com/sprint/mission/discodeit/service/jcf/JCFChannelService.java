package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.ChannelUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service("jcfChannelService")
@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public ChannelDTO createChannel(ChannelCreateDTO channelCreateDTO) {
        Channel channel = new Channel(
                UUID.randomUUID(),
                channelCreateDTO.getName(),
                channelCreateDTO.getDescription(),
                channelCreateDTO.getCreatorId(),
                false,
                Instant.now(),
                new ArrayList<>()
        );
        channelRepository.save(channel);
        return convertToDTO(channel);
    }

    @Override
    public List<ChannelDTO> readAll() {
        return channelRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public Optional<ChannelDTO> read(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(this::convertToDTO);
    }

    @Override
    public void update(UUID channelId, ChannelUpdateDTO channelUpdateDTO) {
        channelRepository.findById(channelId).ifPresent(channel -> {
            if (channel.isPrivate()) {
                throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
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
                .map(this::convertToDTO)
                .toList();
    }

    private ChannelDTO convertToDTO(Channel channel) {
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
}
