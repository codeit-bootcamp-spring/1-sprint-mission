package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
@Profile("Jcf")
@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> map; //채널 리스트
    private final Map<UUID, List<UUID>> messages = new HashMap<>();

    public JCFChannelRepository() {
        this.map = new HashMap<>();
    }

    public List<UUID> findMessagesByChannelId(UUID uuid) {
        List<UUID> messageUuidList = messages.get(uuid); //채널이 가진 메시지 리스트 반환
        return new ArrayList<>(messageUuidList);
    }

    @Override
    public void deleteMessageInChannel(UUID channelId, UUID messageId) {
        messages.get(channelId).remove(messageId);
    }

    public void addMessage(UUID channelId, UUID messageId) {
        Channel channel = findById(channelId);
        if (channel == null) {
            System.out.println("채널을 찾을 수 없습니다.");
            return;
        }
        messages.get(channelId).add(messageId);
        System.out.println("성공적으로 메시시 추가되었습니다.");
    }

    public boolean validationUUID(UUID channelId) {
        return map.containsKey(channelId);
    }

    @Override
    public Channel save(String channelName, ChannelType type) {
        Channel newChannel = new Channel(channelName, type);
        map.put(newChannel.getId(), newChannel);
        messages.put(newChannel.getId(), new ArrayList<>());
        return newChannel;
    }//Public

    @Override
    public Channel save(ChannelType type) {
        Channel newChannel = new Channel(type);
        map.put(newChannel.getId(), newChannel);
        messages.put(newChannel.getId(), new ArrayList<>());
        return newChannel;
    }//Private

    @Override
    public Channel findById(UUID id) {
        return map.get(id);
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> collect = map.values().stream().toList();
        return new ArrayList<>(collect);
    }

    @Override
    public boolean delete(UUID channelId) {
        if (map.containsKey(channelId)) {
            map.remove(channelId);
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
        map.replace(id, channel);
    }

}
