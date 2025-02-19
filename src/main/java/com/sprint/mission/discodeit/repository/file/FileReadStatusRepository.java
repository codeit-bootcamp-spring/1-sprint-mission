package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.UUID;

@ConditionalOnProperty(name = "app.readStatus-repository", havingValue = "file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    // 외부에서 생성자 접근 불가
    public FileReadStatusRepository() {
    }

    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String allReadStatusMapRepository = "Channel\\ChannelReadStatusMap\\";

    //채널 uuid를 이름으로 직렬화되어있는 channelReadStatusMap 객체를 반환. 채널마다 맵이 존재하며, 유저uuid를 키로, 해당 readStatus객체를 밸류로 가지고있음
    @Override
    public HashMap<UUID, ReadStatus> getChannelReadStatusMap(UUID channelId){
        return (HashMap<UUID, ReadStatus>)fileIOHandler.deserializeHashMap(allReadStatusMapRepository+channelId.toString());
    }

    //readStatus 반환
    @Override
    public ReadStatus getReadStatus(UUID channelId, UUID userId){
        HashMap<UUID, ReadStatus> channelMap = getChannelReadStatusMap(channelId);
        return channelMap.get(userId);
    }

    //새 채널이 만들어질 때 그 채널의 readStatus들을 저장하는 맵 생성.
    @Override
    public boolean addChannelReadStatusMap(UUID channelId, HashMap<UUID, ReadStatus> readStatusMap){
        return fileIOHandler.serializeHashMap(readStatusMap,allReadStatusMapRepository+channelId.toString());
    }

    @Override
    public boolean saveReadStatus(UUID channelId, UUID userId, ReadStatus readStatus){
        if (channelId == null || userId == null || readStatus == null) {
            return false;
        }
        HashMap<UUID, ReadStatus> readStatusMap = getChannelReadStatusMap(channelId);
        readStatusMap.put(userId, readStatus);
        return fileIOHandler.serializeHashMap(readStatusMap,channelId.toString());
    }

    @Override
    public boolean deleteReadStatus(UUID channelId, UUID userId){
        HashMap<UUID, ReadStatus> readStatusMap = getChannelReadStatusMap(channelId);
        readStatusMap.remove(userId);
        return fileIOHandler.serializeHashMap(readStatusMap,channelId.toString());
    }

    @Override
    public boolean deleteChannelReadStatusMap(UUID channelId) {
        return fileIOHandler.deleteFile(allReadStatusMapRepository + channelId.toString());
    }

}
