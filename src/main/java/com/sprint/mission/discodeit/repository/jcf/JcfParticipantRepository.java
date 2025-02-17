package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.entity.Participant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Repository
public class JcfParticipantRepository {
    Participant participant = new Participant();
    private Map<UUID, Set<UUID>> participantAndChannels;

    public JcfParticipantRepository() {
        this.participantAndChannels = new HashMap<>();
    }

    public Set<UUID> participants(UUID userId) {
        Set<UUID> userSet= new HashSet<>();
        userSet.add(userId);
        return userSet;
    }

    public void create(UUID channelId,Set<UUID> userSet) {
        participantAndChannels.put(channelId,userSet);
    }

    public Set<UUID> getParticipants(UUID channelId) {
        return participantAndChannels.get(channelId);
    }

    public List<UUID> getParticipationChannels(UUID userId) {
        List<UUID> result=new ArrayList<>();
        for (Map.Entry<UUID, Set<UUID>> entry : participantAndChannels.entrySet()) {
            UUID channelId = entry.getKey();
            Set<UUID> userSet = entry.getValue();
            if(userSet.contains(userId)) {
                result.add(channelId);
            }
        }
        return result;
    }
}
