package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.Setter;

import java.io.File;
import java.util.*;

public class JcfUserService implements UserService {
    //public final Map<UUID,User> map;
    @Setter
    private JcfMessageService jcfMessageService;
    private final JCFUserRepository jcfUserRepository;
    private static volatile JcfUserService instance;

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
    public UserDto createUser(UserDto paramUserDto) {
        UserDto userDto = vaildateUser(paramUserDto.userName(), paramUserDto.email());
        if (userDto != null) return userDto;
        User savedUser = jcfUserRepository.save(paramUserDto.userName(), paramUserDto.email());
        return new UserDto(savedUser);
    }

    private UserDto vaildateUser(String userName, String email) {
        List<User> users = jcfUserRepository.findAll();
        if (users.stream().anyMatch(u -> u.getUserName().equals(userName))) {
            System.out.println("이미 존재하는 사용자입니다.");
            User user = users.stream().filter(s -> s.getUserName().equals(userName)).findAny().get();
            return new UserDto(user);
        }
        if (users.stream().anyMatch(u -> u.getEmail().equals(email))) {
            System.out.println("이미 존재하는 이메일입니다.");
            User user = users.stream().filter(s -> s.getEmail().equals(email)).findAny().get();
            return new UserDto(user);
        }
        return null;
    }

    @Override
    public UserDto getUser(UUID id) {
        return new UserDto(jcfUserRepository.findUserById(id));
        //return map.get(id);
    }

    @Override
    public List<UserDto> getUsers() {
        return jcfUserRepository.findAll().stream().map(UserDto::new).toList(); //Dto로 바꿨으니 new 연산자로 반환 안해도 될까?
    }

    @Override
    public void updateUser(UserDto userDto) {
        if(jcfUserRepository.findAll().stream().map(User::getId).toList().contains(userDto.id())){
            jcfUserRepository.update(userDto.id(), userDto.userName(), userDto.email());
        }else {
            System.out.println("유저를 찾을 수 없습니다.");
        }
    }

    @Override
    public void deleteUser(UUID id) {
        if(jcfUserRepository.findAll().stream().map(User::getId).toList().contains(id)){
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
