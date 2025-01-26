package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCF_Message;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class JCF_user implements UserService {

    private final List<User> userList;
    private Set<String> nameSet = new HashSet<>();

    public JCF_user() {
        userList = new ArrayList<>();
    }

    @Override
    public void creat(String name) {
        User user = new User(name);
        if (!nameSet.add(name)) {
            System.out.println("User name duplication!");
        } else {
            userList.add(user);
            System.out.println(userList);
        }

    }

    @Override
    public void delete(UUID userId, JCF_message jcfMessage) {
        Optional<User> getUser = userList.stream().filter(user1 -> user1.getId().equals(userId)).findFirst();
        if (getUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        else {
            jcfMessage.DeleteMessageList(getUser.get().getUserMessageIdList());
            userList.remove(getUser.get());
        }

    }
    //여기아님
    @Override
    public String getName(UUID userId) {
        return userList.stream()
            .filter(user -> user.getId().equals(userId))
            .map(User::getName)
            .findFirst()
            .orElse(null);
    }

    @Override
    public void update(UUID userId, String name) {
        userList.stream().filter(user -> user.getId().equals(userId) && nameSet.add(user.getName()))
            .forEach(user -> {nameSet.remove(user.getName());
                user.updateName(name);});
    }

    @Override
    public void addMessage(UUID messageId, UUID userId) {
        userList.stream().filter(user -> user.getId().equals(userId))
            .forEach(user -> user.addMessage(messageId));
    }

    @Override
    public void addChannel(UUID channelId, UUID userId) {
        userList.stream().filter(user -> user.getId().equals(userId))
            .forEach(user -> user.addChannel(channelId));
    }

    @Override
    public UUID write(String name) {
        Optional<User> user = userList.stream().filter(user1 -> user1.getName().equals(name)).findFirst();
        if (user.isPresent()) {
            return user.get().getId();
        }
        else {
            System.out.println("Name not found");
            return null;
        }
    }
}
