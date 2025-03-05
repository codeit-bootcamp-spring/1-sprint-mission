package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class JCFReadStatusRepository {
    private List<ReadStatus> statuses = new ArrayList<>();

    public void createStatus(UUID channelId, List<UUID> userList, UUID readUserId){
        ReadStatus readStatus = new ReadStatus();
        readStatus.setChannelId(channelId);
        readStatus.setUsers(userList, readUserId);
        readStatus.setCreatedAt(Instant.now());

        statuses.add(readStatus);
    }

    public void addUser(UUID channelId, UUID userId) {
        for(ReadStatus readStatus : statuses){
            if(readStatus.getChannelId().equals(channelId)){
                readStatus.getCheckUserReadStatus().put(userId,true);
            }
        }
    }

    public void deleteStatus(UUID deleteChannelId) {
        statuses.removeIf(status -> status.getChannelId().equals(deleteChannelId));
    }

    public void deleteUser(UUID id) {
        for(ReadStatus readStatus : statuses){
            readStatus.getCheckUserReadStatus().remove(id);
        }
    }

    public boolean checkUser(UUID channelId ,UUID id) {
        for(ReadStatus readStatus : statuses){
            if(readStatus.getChannelId().equals(channelId) && readStatus.getCheckUserReadStatus().containsKey(id)) return true;
        }

        return false;
    }

    public void findChannel(UUID channelId) {
        for(ReadStatus readStatus : statuses){
            if(readStatus.getChannelId().equals(channelId)){
                System.out.println(readStatus.getCheckUserReadStatus().keySet());
            }
        }
    }

    public ReadStatus findStatus(UUID channelId){
        for(ReadStatus readStatus : statuses){
            if(readStatus.getChannelId().equals(channelId)) return readStatus;
        }


        return null;
    }

    public List<UUID> findByUserId(UUID userId) {
        List<UUID> list = new ArrayList<>();

        for(ReadStatus readStatus : statuses){
            if(readStatus.getCheckUserReadStatus().containsKey(userId)) list.add(readStatus.getChannelId());
        }

        return list;
    }
}
