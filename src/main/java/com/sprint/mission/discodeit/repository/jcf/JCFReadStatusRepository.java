package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.*;

public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data = new HashMap<>();

    @Override
    public void save(ReadStatus request) {
        data.put(request.getId(), request);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<ReadStatus> findByUserIdAndChannelId(UUID userid, UUID channelId) {
        List<ReadStatus> result = new ArrayList<>();
        for(ReadStatus status : data.values()){
            if(status.getUserId().equals(userid) && status.getChannelId().equals(channelId)){
                result.add(status);
            }
        }
        return result;
    }
}
