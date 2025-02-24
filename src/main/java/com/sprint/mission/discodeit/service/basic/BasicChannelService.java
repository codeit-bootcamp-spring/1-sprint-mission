package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelGroup;
import com.sprint.mission.discodeit.entity.Participant;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.web.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.web.dto.PrivateChannelDto;
import com.sprint.mission.discodeit.web.dto.PublicChannelDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Channel createPublicChannel(PublicChannelDto channelParam,Participant... participants) {
        if (channelParam.getChannelName().trim().isEmpty()) {
            throw new IllegalArgumentException("채널 이름을 입력해주세요.");
        }
        if (channelParam.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("채널 설명을 입력해주세요.");
        }
        Channel channel = new Channel(channelParam.getId(),channelParam.getChannelName(), channelParam.getDescription(), channelParam.getGroup());
        for (Participant participant : participants) {
            channel.addParticipant(participant);
            ReadStatus readStatus = new ReadStatus(participant.getUser().getId(), channel.getId());
            readStatusRepository.createReadStatus(readStatus);
        }
        channelRepository.createChannel(channel);
        return channel;
    }


    @Override
    public Channel createPrivateChannel(PrivateChannelDto channelParam,Participant... participants) {
        Channel channel = new Channel(channelParam.getId(),channelParam.getGroup());
        for (Participant participant : participants) {
            channel.addParticipant(participant);
            ReadStatus readStatus = new ReadStatus(participant.getUser().getId(), channel.getId());
            readStatusRepository.createReadStatus(readStatus);
        }
        channelRepository.createChannel(channel);
        return channel;
    }

    @Override
    public Optional<?> findById(UUID id) {
        Channel findchannel = channelRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 ID의 채널을 찾을 수 없습니다."));
        List<ReadStatus> allByChannelId = readStatusRepository.findAllByChannelId(findchannel.getId());
        ReadStatus lastStatus = allByChannelId.get(allByChannelId.size() - 1);
        if (findchannel.isPublic(findchannel)) {
            return Optional.of(new PublicChannelDto(findchannel,lastStatus.getLastReadAt()));
        } else if (findchannel.getGroup() == ChannelGroup.PRIVATE) {
            return Optional.of(new PrivateChannelDto(findchannel,lastStatus.getLastReadAt()));
        }
        return Optional.empty();
    }

    @Override
    public List<PublicChannelDto> findAllPublicChannels() {
        List<PublicChannelDto> publicChannels = new ArrayList<>();
        List<Channel> all = channelRepository.findAll();
        for (Channel channel : all) {
            List<ReadStatus> allByChannelId = readStatusRepository.findAllByChannelId(channel.getId());
            ReadStatus lastStatus = allByChannelId.get(allByChannelId.size() - 1);
            if(channel.getGroup().equals(ChannelGroup.PUBLIC)) {
                publicChannels.add(new PublicChannelDto(channel,lastStatus.getLastReadAt()));
            }
        }
        return publicChannels;
    }

    @Override
    public List<PrivateChannelDto> findAllPrivateChannels() {
        List<PrivateChannelDto> channels = new ArrayList<>();
        List<Channel> all = channelRepository.findAll();
        for (Channel channel : all) {
            List<ReadStatus> allByChannelId = readStatusRepository.findAllByChannelId(channel.getId());
            ReadStatus lastStatus = allByChannelId.get(allByChannelId.size() - 1);
            if (channel.getGroup().equals(ChannelGroup.PRIVATE)) {
                channels.add(new PrivateChannelDto(channel,lastStatus.getLastReadAt()));
            }
        }
        return channels;
    }

    @Override
    public List<UUID> findAllByLoginId(String loginId) {
        List<UUID> result = new ArrayList<>();
        User user= userRepository.findByloginId(loginId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        List<Channel> all = channelRepository.findAll();
        for (Channel channel : all) {
            List<Participant> participants = channel.getParticipants();
            for (Participant participant : participants) {
                if (participant.getUser().equals(user)) {
                    result.add(channel.getId());
                }
            }
        }
        return result;
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
        messageRepository.deleteMessage(channelId);
        channelRepository.deleteChannel(channelId);
    }
    private void validateChannelExits(UUID uuid) {
        if (channelRepository.findById(uuid).isEmpty()) {
            throw new IllegalArgumentException("해당 채널이 존재하지 않습니다.");
        }
    }
}
