package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface EntityFactory {
    User createUser(String userName, String userEmail, String userPassword);

    Channel createChannel(String channelName, User owner, List<User> userList);

    Message createMessage(String title, String content, User sender, User receiver);
}
