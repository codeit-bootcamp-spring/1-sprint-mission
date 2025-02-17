package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.entity.Channel;
import com.sprint.mission.discodeit.dto.entity.Participant;
import com.sprint.mission.discodeit.dto.entity.ReadStatus;
import com.sprint.mission.discodeit.dto.form.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.form.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.form.PublicChannelDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfParticipantRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final JcfParticipantRepository paricipantRepository;

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
        participate(userId, channel);
    }

    @Override
    public void createPrivateChannel(PrivateChannelDto channelParam,UUID userId) {
        if (!channelParam.getChannelGroup().equals("PRIVATE")) {
            log.info("PRIVATE 채널이 아닙니다.");
            return;
        }
        Channel channel = new Channel(channelParam.getChannelGroup());
        channelRepository.createChannel(channel.getId(), channel);
        participate(userId, channel);
        Set<UUID> participants = paricipantRepository.getParticipants(channel.getId());
        for (UUID participant : participants) {
            ReadStatus readStatus = new ReadStatus(participant,channel.getId());
            readStatusRepository.createReadStatus(participant,readStatus);
        }
    }

    private void participate(UUID userId, Channel channel) {
        Set<UUID> userSet = paricipantRepository.participants(userId);
        paricipantRepository.create(channel.getId(), userSet);
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
        return paricipantRepository.getParticipationChannels(userId);
    }

    @Override
    public void updateChannel(UUID id, ChannelUpdateDto channelParam) {
        validateChannelExits(id);
        channelRepository.updateChannel(id, channelParam);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        validateChannelExits(channelId);
        channelRepository.findById(channelId);
        channelRepository.deleteChannel(channelId);
    }
    private void validateChannelExits(UUID uuid) {
        if (!channelRepository.findById(uuid).isPresent()) {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
