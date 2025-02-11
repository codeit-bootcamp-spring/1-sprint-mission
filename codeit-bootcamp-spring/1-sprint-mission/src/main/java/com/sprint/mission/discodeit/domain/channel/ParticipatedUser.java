package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.domain.user.User;
import java.util.ArrayList;
import java.util.List;

public class ParticipatedUser {

    private final List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

}
