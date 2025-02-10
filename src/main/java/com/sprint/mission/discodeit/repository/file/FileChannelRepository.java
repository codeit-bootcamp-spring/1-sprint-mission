package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;

@Repository
public class FileChannelRepository implements ChannelRepository {

    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String mainChannelRepository = "Channel\\mainOIChannelRepository";


    /* 외부에서 생성자 접근 불가
    private FileChannelRepository() {
        fileIOHandler.serializeHashMap(new HashMap<UUID, Channel>(), mainChannelRepository);
    }
    // 레포지토리 객체 LazyHolder 싱글톤 구현.
    private static class FileChannelRepositoryHolder {
        private static final FileChannelRepository INSTANCE = new FileChannelRepository();
    }
    // 외부에서 호출 가능한 싱글톤 인스턴스.
    public static FileChannelRepository getInstance() {
        return FileChannelRepository.FileChannelRepositoryHolder.INSTANCE;
    } */



    // I/O로 생성된 모든 채널 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() {
        return (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
    }

    // 특정 채널객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public Channel getChannel(UUID channelId) {
        if (channelId==null) {
            return null;
        }
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        return channelsMap.get(channelId);
    }

    // 특정 채널객체 여부 확인 후 삭제
    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelId==null) {
            return false;
        }
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        channelsMap.remove(channelId);
        fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);

        return true;
    }

    // 전달받은 채널객체 null 여부 확인 후 채널 해쉬맵에 추가.
    @Override
    public boolean addChannel(Channel channel) {
        if (channel == null) {
            return false;
        }
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        channelsMap.put(channel.getId(), channel);
        fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);
        return true;
    }

    //채널 존재여부 반환
    @Override
    public boolean isChannelExist(UUID channelId) {
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        if (channelId==null || channelsMap.containsKey(channelId) == false) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        if (channelId==null || memberId==null) {
            return false;
        }
        channelsMap.get(channelId).getMembers().add(memberId);
        fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);
        return true;
    }


}
