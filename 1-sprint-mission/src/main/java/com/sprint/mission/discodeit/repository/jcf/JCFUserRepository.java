package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.*;



public class JCFUserRepository {
//    private static final JCFUserRepository INSTANCE = new JCFUserRepository();
//    private JCFUserRepository() {}
//
//    public static JCFUserRepository getInstance() {
//        return INSTANCE;
//    }
    JCFUserStatusRepository statusRepository;
    JCFBinaryContentRepository binaryContentRepository;

    private final List<User> users = new ArrayList<>();

    public User createUser(UserDto userDto) {
        User user = new User();

        for(User u : users){
            if(u.getName().equals(user.getName())){
                return null;
            }
        }
        user.setName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if(!userDto.getEmail().matches(emailRegex)) return null;
        user.setEmail(userDto.getEmail());
        for(User u : users){
            if(u.getId().equals(user.getId())){
                while(!u.getId().equals(user.getId())){
                    user.setId(UUID.randomUUID());
                }
            }
        }

        users.add(user);
        if(userDto.getContainContent()){
            binaryContentRepository.saveUserContent(user.getId());
        }

        return user;
    }

    public boolean update(UUID id, String newNickname) {
        for(User userName : users){
            if(userName.getName().equals(newNickname)) return false;
        }

        for (User user : users) {
            if (user.getId().equals(id)) {
                user.setName(newNickname);
                user.setUpdatedAt(Instant.now());
                break;
            }
        }

        return true;
    }

    public boolean checkUserId(UUID id){
        for(User user : users){
            if(user.getId().equals(id)){
                return true;
            }
        }

        return false;
    }

    public void deleteUser(UUID id) {
        for(User user : users){
            if(user.getId().equals(id)){
                users.remove(user);
                binaryContentRepository.deleteUser(id);
                break;
            }
        }
    }

    public User find(UserDto userDto){
        for(User user : users){
            if(user.getName().equals(userDto.getUserName()) && user.getPassword().equals(userDto.getPassword())){
                System.out.println(user + "status : " + statusRepository.find(userDto));
                return user;
            }
        }

        return null;
    }

    public void findAll(){
        for(User user : users){
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            System.out.println(user + "status : " + statusRepository.find(userDto));
        }
    }

}
