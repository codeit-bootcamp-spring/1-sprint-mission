package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.UUID;

@Getter
@Setter
public class ReadStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 7397482458368623895L;
    private UUID statusId;
    private UUID channelId;
    private List<UUID> messageList;
    private Map<UUID,Boolean> checkUserReadStatus = new HashMap<>();
    private Instant createdAt;
    private Instant updatedAt;

    public void setUsers(List<UUID> list, UUID userId){
        for(UUID uid : list){
            if(uid.equals(userId)){
                checkUserReadStatus.put(uid,true);
            }
            else{
                checkUserReadStatus.put(uid,false);
            }
        }
    }

    public void setUser(UUID userId){
        for(UUID uid : checkUserReadStatus.keySet()){
            if(uid.equals(userId)){
                checkUserReadStatus.put(uid,true);
                return;
            }
        }
        throw new RuntimeException();
    }

}
