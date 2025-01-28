package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {

    // I/O로 생성된 모든 채널 객체가 담기는 해쉬맵
    private static final HashMap<UUID, Channel> channelsMap = new HashMap<UUID, Channel>();

    // 외부에서 생성자 접근 불가
    private FileChannelRepository() {}

    // 레포지토리 객체 LazyHolder 싱글톤 구현.
    private static class FileChannelRepositoryHolder {
        private static final FileChannelRepository INSTANCE = new FileChannelRepository();
    }

    // 외부에서 호출 가능한 싱글톤 인스턴스.
    public static FileChannelRepository getInstance() {
        return FileChannelRepository.FileChannelRepositoryHolder.INSTANCE;
    }

    // I/O로 생성된 모든 채널 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() {
        return channelsMap;
    }

    // 특정 채널객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public Channel getChannel(UUID channelId) {
        if (channelId==null || channelsMap.containsKey(channelId)==false) {
            return null;
        }
        return channelsMap.get(channelId);
    }

    // 특정 채널객체 여부 확인 후 삭제. 불값 반환
    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelId==null || channelsMap.containsKey(channelId) == false) {
            return false;
        }
        channelsMap.remove(channelId);
        return true;
    }

    // 전달받은 채널객체 null 여부 확인 후 채널 해쉬맵에 추가.
    @Override
    public boolean addChannel(Channel channel) {
        if (channel == null) {
            return false;
        }
        channelsMap.put(channel.getId(), channel);
        return true;
    }

    //채널 존재여부 반환
    @Override
    public boolean isChannelExist(UUID channelId) {
        if (channelId==null || channelsMap.containsKey(channelId) == false) {
            return false;
        }
        return true;
    }

    // 전달받은 채널맵 null 여부 확인 후 기존 채널맵에 추가.
    public boolean addChannels(HashMap<UUID, Channel> channels) {
        if (channels == null) {
            return false;
        }
        channelsMap.putAll(channels);
        return true;
    }
}
