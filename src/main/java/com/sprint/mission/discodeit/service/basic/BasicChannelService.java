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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("basicChannelService")
@Primary
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

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

    // ✅ 특정 사용자가 볼 수 있는 채널 목록 조회 (추가된 부분)
    @Override
    public List<ChannelDTO> getChannelsForUser(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel -> channel.isPublic() || channel.getMembers().contains(userId)) // 공개 채널 또는 사용자가 속한 비공개 채널만 필터링
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
