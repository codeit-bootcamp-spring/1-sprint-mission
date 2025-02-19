package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.domain.user.User;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ParticipatedUser {

    private final Map<UUID, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void removeUser(User user) {
        users.remove(user.getId());
    }

    public Set<UUID> getParticipatedUserId() {
        return Set.copyOf(users.keySet());
    }
}
