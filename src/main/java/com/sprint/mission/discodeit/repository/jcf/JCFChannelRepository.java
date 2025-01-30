package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.ArrayList;
import java.util.List;


public class JCFChannelRepository {
    private static JCFChannelRepository instance;
    private final List<Channel> channels;

    private JCFChannelRepository(){
        this.channels = new ArrayList<>();
    }

    public static synchronized JCFChannelRepository getInstance() {
        if (instance == null) {
            instance = new JCFChannelRepository();
        }
        return instance;
    }

    public void save(Channel channel){
        channels.add(channel);
    }

    public void delete(Channel channel){
        channels.remove(channel);
    }

    public List<Channel> load() {
        return new ArrayList<>(channels);
    }

    public boolean exists(Channel channel) {
        return channels.contains(channel);
    }

    public void update(Channel channel) {
        int index = channels.indexOf(channel);
        if (index != -1) {
            channels.set(index, channel);
        }
    }

}
