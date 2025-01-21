package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.IdNotFoundException;
import com.sprint.mission.discodeit.exception.ValidationException;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class JCFUserRepository implements UserRepository {
    private static final String USER_SAVE_FILE = "config/user.ser";
    private final Map<UUID, User> data;

    public JCFUserRepository() { this.data = loadFromFile(); }

    private Map<UUID, User> loadFromFile(){
        File file = new File(USER_SAVE_FILE);
        if(!file.exists()){
            return new HashMap<>();
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)
        ){
            List<User> users = (List<User>) ois.readObject();
            Map<UUID, User> userMap = new HashMap<>();
            for(User user : users){
                userMap.put(user.getId(), user);
            }

            return userMap;
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(){
        List<User> users = new ArrayList<>(data.values());

        try (FileOutputStream fos = new FileOutputStream(USER_SAVE_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(users);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean save(User user) {
//        if(data.values().stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
//            System.out.println("중복된 이메일이 존재합니다. : " + user.getEmail());
//            return false;
//        }

        data.put(user.getId(), user);
        saveToFile();
        System.out.println(user.getUsername() + "님의 사용자 등록이 완료되었습니다.");
        return true;
    }

    @Override
    public User findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User modify(UUID id, User modifiedUser){
        if(data.containsKey(id)){
            User existingUser = data.get(id);

            existingUser.setUsername(modifiedUser.getUsername());
            existingUser.setEmail(modifiedUser.getEmail());
            existingUser.setPhoneNumber(modifiedUser.getPhoneNumber());
            existingUser.setAddr(modifiedUser.getAddr());
            existingUser.setAge(modifiedUser.getAge());
            existingUser.setHobby(modifiedUser.getHobby());
            existingUser.setInterest(modifiedUser.getInterest());
            modifiedUser.update();

            return existingUser;
        }else{
            System.out.println(new IdNotFoundException("아이디가 존재하지 않습니다."));
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

}
