package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.entity.ChannelGroup;
import com.sprint.mission.discodeit.domain.entity.Participant;
import com.sprint.mission.discodeit.web.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.web.dto.PrivateChannelDto;
import com.sprint.mission.discodeit.web.dto.PublicChannelDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class FileChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel createPublicChannel(PublicChannelDto channelParam,Participant... participants) {
        if (channelParam.getChannelName().trim().isEmpty()) {
            throw new IllegalArgumentException("채널 이름을 입력해주세요.");
        }
        if (channelParam.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("채널 설명을 입력해주세요.");
        }
        Channel channel = new Channel(channelParam.getId(),channelParam.getChannelName(), channelParam.getDescription(), channelParam.getGroup());
        channelRepository.createChannel(channel);
        return channel;
    }
    @Override
    public Channel createPrivateChannel(PrivateChannelDto channelParam,Participant... participants) {

        Channel channel = new Channel(channelParam.getId(),channelParam.getGroup());
        channelRepository.createChannel(channel);
        return channel;
    }

    @Override
    public Optional<?> findById(UUID id) {
        return Optional.empty();
    }


    @Override
    public List<PrivateChannelDto> findAllPrivateChannels() {
        return List.of();
    }

    @Override
    public List<PublicChannelDto> findAllPublicChannels() {
        return List.of();
    }

    @Override
    public List<UUID> findAllByLoginId(String loginId) {
        return List.of();
    }


    @Override
    public void updateChannel(UUID id, ChannelUpdateDto channelParam) {
        validateFileChannelExits(id);
        channelRepository.updateChannel(id, channelParam);
    }

    @Override
    public void deleteChannel(UUID id) {
        validateFileChannelExits(id);
        channelRepository.deleteChannel(id);
    }
    private void validateFileChannelExits(UUID uuid) {
        if (!channelRepository.findById(uuid).isPresent()) {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
