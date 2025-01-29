package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> list; //채널 리스트
    private final Map<UUID, List<UUID>> messages = new HashMap<>();
    //private JcfMessageService jcfMessageService;
    //private static volatile JcfChannelService instance;

    public JCFChannelRepository(Map<UUID, Channel> list) {
        this.list = list;
    }
    public List<UUID> messages(UUID uuid) {
        Channel channel = findById(uuid);
        if (channel == null) {
            System.out.println("채널을 찾을 수 없습니다.");
            return null;
        }
        List<UUID> messageUuidList = messages.get(channel.getChannelId());
        return new ArrayList<>(messageUuidList);
    }
    public void addMessage(UUID uuid, UUID messageId) {
        Channel channel = findById(uuid);
        if (channel == null) {
            System.out.println("채널을 찾을 수 없습니다.");
            return;
        }
        messages.get(uuid).add(messageId);
        System.out.println("성공적으로 메시시 추가되었습니다.");
    }

    public boolean validationUUID(UUID channelId) {
        return list.containsKey(channelId);
    }

    @Override
    public UUID save(String channelName) {
        Channel channel = new Channel(channelName);
        list.put(channel.getChannelId(), channel);
        messages.put(channel.getChannelId(), new ArrayList<>());
        return channel.getChannelId();
    }

    @Override
    public Channel findById(UUID id) {
        return list.get(id);
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> collect = list.values().stream().toList();
        return new ArrayList<>(collect);
    }

    @Override
    public boolean delete(UUID channelId) {
        if (list.containsKey(channelId)) {
            list.remove(channelId);
            return true;
        } else {
            System.out.println("채널을 찾을 수 없습니다.");
            return false;
        }
    }

    @Override
    public void update(UUID id, String channelName) {
        Channel channel = findById(id);
        channel.update(channelName);
        list.replace(id, channel);
    }
}
