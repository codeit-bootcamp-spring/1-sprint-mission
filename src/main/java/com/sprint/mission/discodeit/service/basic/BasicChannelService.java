package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    //생성
    //PUBLIC 채널
    public Channel createPublicChannel(CreatePublicChannelDto publicChannelDto) {
        Channel channel = new Channel(publicChannelDto.getChannelName(), publicChannelDto.getDescription(), ChannelType.PUBLIC);
        if (channelRepository.saveChannel(channel)) {
            return channel;
        }
        return null;
    }
    //PRIVATE 채널
    public Channel createPrivateChannel(CreatePrivateChannelDto privateChannelDto) {
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        if (channelRepository.saveChannel(channel)) {
            for (UUID userId : privateChannelDto.getUserIds()) {
                ReadStatus readStatus = new ReadStatus(userId, channel.getId());
                readStatusRepository.save(readStatus);
            }
            return channel;
        }
        return null;
    }

    // 수정
    public Channel updateChannel(UpdateChannelDto updateChannelDto) {
        Channel updatedChannel = channelRepository.loadChannel(updateChannelDto.getChannelId());
        if (updatedChannel != null && updatedChannel.getType() == ChannelType.PUBLIC) {
            updatedChannel.updateChannelName(updateChannelDto.getChannelName());
            updatedChannel.updateDescription(updateChannelDto.getDescription());
            if (channelRepository.saveChannel(updatedChannel)) {
                System.out.println("channel name updated");
                return updatedChannel;
            }
        }
        return null;
    }

    // 조회
    public ChannelResponse findChannel(UUID channelId) {
        Channel channel = channelRepository.loadChannel(channelId);
        List<UUID> userIds;
        if (channel != null) {
            if (channel.getType() == ChannelType.PRIVATE) {
                userIds = readStatusRepository.findByChannelId(channelId)
                        .stream().map(ReadStatus::getUserId).collect(Collectors.toList());
            } else {
                userIds = List.of();
            }
            return new ChannelResponse(channel, userIds);
        }
        return null;
    }
    public List<ChannelResponse> findAllChannelsByUserId(UUID userId) {
        List<Channel> channels = channelRepository.loadAllChannels();
        List<UUID> userIds = readStatusRepository.findByUserId(userId).stream().map(ReadStatus::getChannelId).toList();
        /*
        return channelRepository.loadAllChannels().stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC) || userIds.contains(channel.getId()))

         */
         // 어떻게 해야할지 모르겠음 ...
        return null;
    }


    // 삭제
    public void deleteChannel(Channel channel) {
        // Message, ReadStatus도 같이 삭제 ... 못했음
        if (channelRepository.deleteChannel(channel)) {
            System.out.println(channel.getChannelName() + " channel deleted");
        }
    }
}
