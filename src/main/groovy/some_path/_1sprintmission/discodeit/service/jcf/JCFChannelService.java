package some_path._1sprintmission.discodeit.service.jcf;

import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void createChannel(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> getChannel(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> getChannelAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID id, Channel updatedChannel) {
        if (data.containsKey(id)) {
            data.put(id, updatedChannel);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }
}
