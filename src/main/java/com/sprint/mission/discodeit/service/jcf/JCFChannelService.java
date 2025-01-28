package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> channelList;

    public JCFChannelService() {
        this.channelList = new HashMap<>();
    }

    @Override
    public Channel createChannel(String name) {
        //name의 null과 ""방어
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("채널명을 null 또는 빈 문자열로 생성할 수 없습니다.");
        }

        Channel channel = new Channel(name);
        channelList.put(channel.getChannelId(), channel);
        System.out.println("채널명: " + channel.getChannelName() + " 채널이 추가되었습니다.");
        return channel;

    }

    @Override
    public Channel readChannel(UUID id) {
        Channel channel = channelList.get(id);
        //null 방어
        if (channel == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다. ID: " + id);
        }
        return channel;
    }

    @Override
    public List<Channel> readAllChannel() {
        return new ArrayList<>(channelList.values());
    }

    @Override
    public Channel modifyChannel(UUID id, String name) {
        Channel target = readChannel(id);
        if (target == null) {
            throw new IllegalArgumentException("수정할 채널을 찾을 수 없습니다. ID: " + id);
        }
        String oriName = target.getChannelName();
        target.updateName(name);
        System.out.println("채널 이름 변경: \"" + oriName + "\" -> \"" + name + "\"");
        return target;
    }

    @Override
    public void deleteChannel(UUID id) {
        Channel target = readChannel(id);
        if (target == null) {
            throw new IllegalArgumentException("삭제할 채널을 찾을 수 없습니다. ID: " + id);
        }
        channelList.remove(id);
        System.out.println("채널 \"" + target.getChannelName() + "\"이 삭제되었습니다.");
    }
}
