package discodeit.jcf;

import discodeit.entity.Channel;
import discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public void create(Channel channel) {
        data.put(channel.getId(), channel);
        System.out.println("Channel created: " + channel.getChannelName());
    }

    @Override
    public Channel read(UUID id) {
        if(!data.containsKey(id)) {
            System.out.println("Channel not found");
            return null;
        }
        System.out.println("Channel read: " + data.get(id).getChannelName());
        return data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, String channelName) {
        Channel channel = data.get(id);
        if (channel != null) {
            System.out.print("Channel updated: " + channel.getChannelName());
            channel.updateChannelName(channelName);
            channel.updateUpdatedAt();
            System.out.println(" -> " + channel.getChannelName());
        }
    }

    @Override
    public void delete(UUID id) {
        if(!data.containsKey(id)) {
            System.out.println("Channel not found for delete");
        }
        data.remove(id);
        System.out.println("Channel deleted");
    }
}
