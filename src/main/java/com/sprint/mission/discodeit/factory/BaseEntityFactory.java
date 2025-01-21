package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public class BaseEntityFactory implements EntityFactory {

    private BaseEntityFactory() {}

    private static class SingleInstanceHolder {
        private static final EntityFactory INSTANCE = new BaseEntityFactory();
    }

    public static EntityFactory getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }

    @Override
    public User createUser(String userName, String userEmail, String userPassword) {
        return new User(userName, userEmail, userPassword);
    }

    @Override
    public Channel createChannel(String channelName, User owner, Map<UUID, User> userList) {
        return new Channel(channelName, owner, userList);
    }

    @Override
    public Message createMessage(String title, String content, User sender, User receiver) {
        return new Message(title, content, sender, receiver);
    }
}
