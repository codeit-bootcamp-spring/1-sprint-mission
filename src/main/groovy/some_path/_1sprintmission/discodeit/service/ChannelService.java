package some_path._1sprintmission.discodeit.service;


import some_path._1sprintmission.discodeit.entiry.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createChannel(Channel channel);
    Optional<Channel> getChannel(UUID id);
    List<Channel> getChannelAll();
    void updateChannel(UUID id, Channel updatedChannel);
    void deleteChannel(UUID id);
}
