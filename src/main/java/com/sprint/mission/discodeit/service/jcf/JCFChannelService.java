package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service("jcfChannelService")
@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

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
    public void createPrivateChannel(UUID creatorId, List<UUID> members) {
        Channel channel = new Channel(
                UUID.randomUUID(),
                "Private Channel",
                "Private Channel for users",
                creatorId,
                true
        );
        channel.setMembers(members);
        channelRepository.save(channel);
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
