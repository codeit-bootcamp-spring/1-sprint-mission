package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JcfUserService implements UserService {
    //public final Map<UUID,User> map;
    private JcfMessageService jcfMessageService;
    private final JCFUserRepository jcfUserRepository;
    private static volatile JcfUserService instance;
    public void setJcfMessageService(JcfMessageService jcfMessageService) {
        this.jcfMessageService = jcfMessageService;
    }
    public static JcfUserService getInstance() {
        if (instance == null) {
            synchronized (JcfUserService.class) {
                if (instance == null) {
                    instance = new JcfUserService(new JCFUserRepository(new HashMap<>()));
                }
            }
        }
        return instance;
    }
    private JcfUserService( JCFUserRepository jcfUserRepository) {
        this.jcfUserRepository = jcfUserRepository;
    }

    @Override
    public UUID createUser(String userName) {
        return jcfUserRepository.save(userName);
        /*User user = new User(userName);
        map.put(user.getUserId(),user);
        return user.getUserId();*/
    }

    @Override
    public User getUser(UUID id) {
        return jcfUserRepository.findUserById(id);
        //return map.get(id);
    }

    @Override
    public List<User> getUsers() {
        List<User> collect = jcfUserRepository.findAll();
        //List<User> collect = map.entrySet().stream().map(s -> s.getValue()).collect(Collectors.toList());
        return new ArrayList<>(collect);
    }

    @Override
    public void updateUser(UUID id, String username) {
        if(jcfUserRepository.findAll().stream().map(User::getId).toList().contains(id)){
            jcfUserRepository.update(id, username);
        }else {
            System.out.println("유저를 찾을 수 없습니다.");
        }
    }

    @Override
    public void deleteUser(UUID id) {
        if(jcfUserRepository.findAll().contains(id)){
            List<Message> messageByUserId = jcfMessageService.getMessagesByUserId(id);
            for (Message message : messageByUserId) {
                jcfMessageService.deleteMessage(message.getId());
            }
            jcfUserRepository.delete(id);
        } else {
            System.out.println("유저를 찾을 수 없습니다.");
        }
    }
}
