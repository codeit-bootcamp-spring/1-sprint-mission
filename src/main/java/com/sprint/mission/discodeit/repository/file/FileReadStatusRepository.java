package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    // 외부에서 생성자 접근 불가
    public FileReadStatusRepository() {
    }

    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String allReadStatusMapRepository = "Channel\\ChannelReadStatusMap\\";

    //채널 uuid를 이름으로 직렬화되어있는 channelReadStatusMap 객체를 반환. 채널마다 맵이 존재하며, 유저uuid를 키로, 해당 readStatus객체를 밸류로 가지고있음
    @Override
    public HashMap<UUID, ReadStatus> getChannelReadStatusMap(UUID channelId) throws IOException {
        return (HashMap<UUID, ReadStatus>)fileIOHandler.deserializeHashMap(allReadStatusMapRepository+channelId.toString());
    }

    @Override
    public ReadStatus getReadStatus(UUID channelId, UUID userId) throws IOException {
        HashMap<UUID, ReadStatus> channelMap = getChannelReadStatusMap(channelId);
        return channelMap.get(userId);
    }


    @Override
    public boolean addChannelReadStatusMap(UUID channelId, HashMap<UUID, ReadStatus> readStatusMap) throws IOException{
        fileIOHandler.serializeHashMap(readStatusMap,allReadStatusMapRepository+channelId.toString());
        return true;
    }

    @Override
    public boolean saveReadStatus(UUID channelId, UUID userId, ReadStatus readStatus) throws IOException {
        if (channelId == null || userId == null || readStatus == null) {
            throw new NullPointerException("saveReadStatus에 전달된 파라미터 일부가 null인 상태로, 유저를 저장하지 못했습니다.");
        }
        HashMap<UUID, ReadStatus> readStatusMap = getChannelReadStatusMap(channelId);
        readStatusMap.put(userId, readStatus);
        fileIOHandler.serializeHashMap(readStatusMap,channelId.toString());
        return true;
    }

    @Override
    public boolean deleteReadStatus(UUID channelId, UUID userId) throws IOException {
        HashMap<UUID, ReadStatus> readStatusMap = getChannelReadStatusMap(channelId);
        if (readStatusMap.remove(userId).equals(null)) {
            throw new NoSuchElementException("해당 uuid를 가진 readStatus 객체가 채널 readStatusMap에 없습니다.");
        }
        fileIOHandler.serializeHashMap(readStatusMap,channelId.toString());
        return true;
    }

    @Override
    public boolean deleteChannelReadStatusMap(UUID channelId) {
        if (channelId == null) {
            return false;
        }
        return fileIOHandler.deleteFile(allReadStatusMapRepository + channelId.toString());
    }

}
