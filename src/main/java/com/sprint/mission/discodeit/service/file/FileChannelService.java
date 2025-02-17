package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.entity.Channel;
import com.sprint.mission.discodeit.dto.form.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.form.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.form.PublicChannelDto;
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
    public void createPublicChannel(PublicChannelDto channelParam,UUID userId) {
        if (channelParam.getChannelName().trim().isEmpty()) {
            log.info("채널 이름을 입력해주세요.");
            return;
        }
        if (channelParam.getDescription().trim().isEmpty()) {
            log.info("채널 설명을 입력해주세요.");
            return;
        }
        Channel channel = new Channel(channelParam.getChannelName(), channelParam.getDescription(), channelParam.getChannelGroup());
        if (!channelParam.getChannelGroup().equals("PUBLIC")) {
            log.info("PUBLIC 채널이 아닙니다.");
            return;
        }
        channelRepository.createChannel(channel.getId(),channel);
    }
    @Override
    public void createPrivateChannel(PrivateChannelDto channelParam,UUID userId) {
        if (!channelParam.getChannelGroup().equals("PRIVATE")) {
            log.info("PRIVATE 채널이 아닙니다.");
            return;
        }
        Channel channel = new Channel(channelParam.getChannelGroup());
        channelRepository.createChannel(channel.getId(), channel);
    }
    @Override
    public Optional<PrivateChannelDto> findPrivateChannel(UUID id) {
        Optional<Channel> optionalChannel = channelRepository.findById(id);
        Channel channel = optionalChannel.get();
        return Optional.of(new PrivateChannelDto(channel));
    }
    @Override
    public Optional<PublicChannelDto> findPublicChannel(UUID id) {
        Optional<Channel> optionalChannel = channelRepository.findById(id);
        Channel channel = optionalChannel.get();
        return Optional.of(new PublicChannelDto(channel));
    }

    @Override
    public List<Channel> findAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public List<UUID> findAllByUserId(UUID userId) {
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
