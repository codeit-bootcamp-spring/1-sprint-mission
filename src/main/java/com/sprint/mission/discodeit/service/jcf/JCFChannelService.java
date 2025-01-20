package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Map<String, Channel> channels = new HashMap<>();
    @Override
    public void createChannel(Channel channel) {
        if (channels.containsKey(channel.getId().toString())) {
            throw new IllegalArgumentException("이미 존재하는 채널입니다 : " + channel.getId());
        }
        channels.put(channel.getId().toString(), channel);
        System.out.println("채널이 생성되었습니다." + channel.getId()+ ", Created At: " + channel.getCreatedAt());
    }
    @Override
    public Channel readChannel(String id) {
        Channel channel = channels.get(id);
        if (!channels.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다. " + id);
        }
        return channel;
    }
    @Override
    public void updateChannel(Channel channel) {

        if(!channels.containsKey(channel.getId().toString())) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다. " + channel.getId());
        }
        channels.put(channel.getId().toString(), channel);
        System.out.println("채널이 업데이트 되었습니다. " + channel.getId() +  ", Updated At: " + channel.getUpdatedAt());
    }

    @Override
    public void deleteChannel(String id) {
        if(!channels.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다. " + id);
        }
        channels.remove(id);
        System.out.println("채널이 삭제되었습니다. " + id);
    }
    @Override
    public void addMember(String channelId, User member) {
        Channel channel = readChannel(channelId);
        channel.addMember(member);
        System.out.println("멤버가 추가되었습니다. " + member.getName());
    }
    @Override
    public void removeMember(String channelId, User member) {
        Channel channel = readChannel(channelId);
        channel.removeMember(member);
        System.out.println("멤버가 삭제되었습니다. " + member.getName());
    }
    public List<Channel> readAllChannel() {
        return channels.values().stream().collect(Collectors.toList());
    };
}
