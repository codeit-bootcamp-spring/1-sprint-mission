package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.Validation;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID,Channel> data; //검색 성능을 위해서 map으로 변경
    private static volatile JCFChannelService instance;

    public JCFChannelService() {
        this.data = new HashMap();
    }

    //싱글톤
    public static JCFChannelService getInstance() {
        if (instance==null){
            synchronized (JCFChannelService.class){
                if(instance==null){
                    instance=new JCFChannelService();
                }
            }
        }
        return instance;
    }

    @Override
    public void createChannel(Channel channel) {
        Validation.validateUserExists(channel.getCreator().getId());
        data.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> getChannelById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID id, Channel updatedChannel) {
        Channel existingChannel = data.get(id);
        if (existingChannel != null) {
            existingChannel.update(updatedChannel.getChannel(),updatedChannel.getDescription());
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }
}
