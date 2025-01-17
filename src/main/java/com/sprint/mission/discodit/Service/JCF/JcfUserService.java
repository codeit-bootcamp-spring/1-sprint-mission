package com.sprint.mission.discodit.Service.JCF;

import com.sprint.mission.discodit.Entity.Message;
import com.sprint.mission.discodit.Entity.User;
import com.sprint.mission.discodit.Service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JcfUserService implements UserService {
    public final Map<UUID,User> map;
    private JcfMessageService jcfMessageService;
    private static volatile JcfUserService instance;
    public void setJcfMessageService(JcfMessageService jcfMessageService) {
        this.jcfMessageService = jcfMessageService;
    }
    public static JcfUserService getInstance() {
        if (instance == null) {
            synchronized (JcfUserService.class) {
                if (instance == null) {
                    instance = new JcfUserService(new HashMap<>());
                }
            }
        }
        return instance;
    }
    private JcfUserService(Map<UUID,User> list) {
        this.map = list;
    }

    @Override
    public UUID createUser(String userName) {
        User user = new User(userName);
        map.put(user.getUserId(),user);
        return user.getUserId();
    }

    @Override
    public User getUser(UUID id) {
        return map.get(id);
    }

    @Override
    public List<User> getUsers() {
        List<User> collect = map.entrySet().stream().map(s -> s.getValue()).collect(Collectors.toList());
        return new ArrayList<>(collect);
    }

    @Override
    public void setUser(UUID id, String username) {
        if(map.containsKey(id)){
            User user = getUser(id);
            user.update(username);
        }else {
            System.out.println("유저를 찾을 수 없습니다.");
        }
    }

    @Override
    public void deleteUser(UUID id) {
        if(map.containsKey(id)){
            List<Message> messageByUserId = jcfMessageService.getMessageByUserId(id);
            for (Message message : messageByUserId) {
                jcfMessageService.deleteMessage(message.getMessageId());
            }
            map.remove(id);
        }else {
            System.out.println("유저를 찾을 수 없습니다.");
        }
    }
}
