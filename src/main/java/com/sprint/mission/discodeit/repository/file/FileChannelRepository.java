package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {

    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String mainChannelRepository = "Channel\\mainOIChannelRepository";

    // I/O로 생성된 모든 채널 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() throws Exception {
        return (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
    }

    // 특정 채널객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public Channel getChannel(UUID channelId) throws Exception{
        HashMap<UUID, Channel> channelsMap = getChannelsMap();

        return Optional.ofNullable(channelsMap.get(channelId)).orElseThrow(()->new NoSuchElementException("해당 uuid를 가진 채널이 존재하지 않습니다."));
    }

    // 특정 채널객체 여부 확인 후 삭제
    @Override
    public boolean deleteChannel(UUID channelId) throws Exception{
        HashMap<UUID, Channel> channelsMap = getChannelsMap();
        //해시맵은 존재하는 key를 삭제하면 삭제한 요소를 반환하지만 없는 key를 삭제하면 null 반환.
        if (channelsMap.remove(channelId)==null){
            throw new NoSuchElementException("해당 uuid를 가진 채널이 존재하지 않습니다.");
        };
        fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);
        return true;
    }

    // 전달받은 채널객체 null 여부 확인 후 채널 해쉬맵에 추가.
    @Override
    public boolean saveChannel(Channel channel) throws Exception{
        if (channel == null) {
            throw new RuntimeException("addChannel()의 파라미터에 전달된 유저가 null인 상태입니다. ");
        }
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        channelsMap.put(channel.getId(), channel);
        fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);
        return true;
    }

    // 채널 존재여부 반환
    @Override
    public boolean isChannelExist(UUID channelId) throws Exception {
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        return channelsMap.containsKey(channelId);
    }

    // 채널 객체에 멤버 추가
    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId) throws Exception{
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        Channel channel = Optional.ofNullable(channelsMap.get(channelId)).orElseThrow(()->new NoSuchElementException("해당 uuid를 가진 채널이 존재하지 않습니다."));
        ArrayList<UUID> membersId = channel.getMembers();
        if (membersId.contains(membersId)==true){
            throw new RuntimeException("해당 멤버는 이미 존재합니다.");
        }
        channel.getMembers().add(memberId);
        fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);
        return true;
    }


    //멤버 삭제
    //todo 멤버를 굳이 숫자가 있는 ArrayList에 넣어야할까? 컬렉션이 어레이리스트이다보니 요소 인덱스 찾은다음에 삭제해야돼서 불편한데? 멤버 리스트의 컬렉션종류 변경을 고려해봐야겠음.
    @Override
    public boolean removeChannelMember(UUID channelId, UUID memberId) throws Exception {
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        Channel channel = Optional.ofNullable(channelsMap.get(channelId)).orElseThrow(()->new NoSuchElementException("해당 uuid를 가진 채널이 존재하지 않습니다."));
        List<UUID> membersId = channel.getMembers();
        int index = membersId.indexOf(memberId);
        if (index == -1){
            throw new NoSuchElementException("해당 uuid를 가진 유저가 존재하지 않습니다.");
        }
        membersId.remove(index);
        fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);
        return false;
    }


}
