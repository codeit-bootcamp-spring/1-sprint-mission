package com.sprint.mission.discodeit.service.file;

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

@Service("fileChannelService")
@RequiredArgsConstructor
public class FileChannelService implements ChannelService {

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
    public void update(UUID channelId, ChannelUpdateDTO channelUpdateDTO) {
        Optional<Channel> optionalChannel = channelRepository.findById(channelId);

        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();

            if (channel.isPrivate()) {
                throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
            }

            channel.setName(channelUpdateDTO.getName());
            channel.setDescription(channelUpdateDTO.getDescription());
            channelRepository.save(channel);
        } else {
            throw new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다.");
        }
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
