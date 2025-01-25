package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JCFChannelRepository implements ChannelRepository {
    private final List<Channel> data;
    public JCFChannelRepository() {
        data = new ArrayList<>();
    }

    // 저장
    public boolean saveChannel(Channel channel){
        try {
            if (channel == null) {
                throw new IllegalArgumentException("channel is null");
            }
            if (data.contains(channel)) {
                deleteChannel(channel);
            }
            data.add(channel);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // 조회
    public Channel loadChannel(Channel channel) {
        return data.stream().filter(d -> d.getId().equals(channel.getId()))
                .findFirst().orElse(null);
    }
    public List<Channel> loadAllChannels() {
        return Collections.unmodifiableList(data);
    }

    // 삭제
    public boolean deleteChannel(Channel channel) {
        try {
            if (!data.contains(channel)) {
                throw new IllegalArgumentException("Channel not found");
            }
            if (data.remove(channel)) {
                return true;
            }
            throw new RuntimeException("Failed to remove Channel");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }


}
