package discodeit.service;

import discodeit.dto.channel.CreatePrivateChannelRequest;
import discodeit.dto.channel.CreatePublicChannelRequest;
import discodeit.dto.channel.UpdateChannelRequest;
import discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(CreatePublicChannelRequest createPublicChannelRequest);
    Channel create(CreatePrivateChannelRequest createPrivateChannelRequest);
    Channel find(UUID channelId);
    List<Channel> findAllByUserId(UUID userId);
    Channel update(UpdateChannelRequest updateChannelRequest);
    void delete(UUID channelId);
    void addParticipant(UUID channelId, UUID userId);
    void deleteParticipant(UUID channelId, UUID userId);
}
