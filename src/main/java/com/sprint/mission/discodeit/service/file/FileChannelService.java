package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.ChannelUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("fileChannelService")
@RequiredArgsConstructor
public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public void createPublicChannel(ChannelCreateDTO channelCreateDTO) {
        Channel channel = new Channel(
                UUID.randomUUID(),
                channelCreateDTO.getName(),
                channelCreateDTO.getDescription(),
                channelCreateDTO.getCreatorId(),
                false // ✅ 공개 채널
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
                .collect(Collectors.toList());
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

    // ✅ 채널 정보 수정 메서드 추가
    @Override
    public void update(UUID channelId, ChannelUpdateDTO channelUpdateDTO) {
        Optional<Channel> optionalChannel = channelRepository.findById(channelId);

        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();

            // ❌ 비공개 채널 수정 불가
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

    // ✅ 특정 사용자가 볼 수 있는 채널 목록 조회 메서드 추가
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
                .collect(Collectors.toList());
    }
}
