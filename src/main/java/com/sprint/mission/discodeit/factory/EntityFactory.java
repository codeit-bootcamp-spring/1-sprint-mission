package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserRole;

import java.util.Map;
import java.util.UUID;

public interface EntityFactory {
    User createUser(String userName, String userEmail, String userPassword);

    User createUser(String userName, String userEmail, String userPassword, UserRole userRole);

    Channel createChannel(UUID channelId);

    Channel createChannel(String channelName, User owner, String channelType, Map<UUID, User> userList);

    Message createMessage(String title, String content, User sender, User receiver);
    Message createMessage(UUID messageId, String title, String content, User sender, User receiver);
}
