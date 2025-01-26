package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.HashMap;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    private final HashMap<UUID, Channel> data = new HashMap<>();

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
        System.out.println("채널 저장 완료 : " + channel.getId());
    }

    @Override
    public void findById(UUID id) {
        if (data.containsKey(id)) {
            System.out.println("channel name : " + data.get(id).getChannelName());
            System.out.println("channel admin : " + data.get(id).getAdminUser().getUserNickName());
        }
    }

    @Override
    public void findAll() {
        for (UUID uuid : data.keySet()) {
            System.out.println("Channel ID : " + uuid);
            System.out.println("Channel Name : " + data.get(uuid).getChannelName());
            System.out.println("Channel Admin : " + data.get(uuid).getAdminUser().getUserNickName());
        }

    }

    @Override
    public void update(Channel channel) {
        // 이 함수가 호출되면 해당 엔티티의 updateAt값을 변경 시점 시간으로 저장해주어야 한다.
        data.put(channel.getId(), channel);
        System.out.println("채널 정보 변경");
        channel.updateUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
