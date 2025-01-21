package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.file.FileService;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository, FileService<User> {
    private static final String USER_SAVE_FILE = "config/user.ser";

    @Override
    public List<User> loadFromFile() {
        File file = new File(USER_SAVE_FILE);
        if(!file.exists()){
            return new ArrayList<>();
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)
        ){
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean saveToFile(List<User> data) {
        try (FileOutputStream fos = new FileOutputStream(USER_SAVE_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(data);
            return true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(User user){
        List<User> users = loadFromFile();
        users.add(user);
        saveToFile(users);
        return true;
    }

    @Override
    public User findById(UUID id) {
        return loadFromFile().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> readAll(){
        return loadFromFile();
    }

    @Override
    public User modify(UUID id, User modifiedUser){
        List<User> users = loadFromFile();

        for(int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i).getId());
            if (users.get(i).getId().equals(id)) {

                User existingUser = users.get(i);

                existingUser.setUsername(modifiedUser.getUsername());
                existingUser.setEmail(modifiedUser.getEmail());
                existingUser.setPhoneNumber(modifiedUser.getPhoneNumber());
                existingUser.setAddr(modifiedUser.getAddr());
                existingUser.setAge(modifiedUser.getAge());
                existingUser.setHobby(modifiedUser.getHobby());
                existingUser.setInterest(modifiedUser.getInterest());
            } else {
                System.out.println("아이디가 존재하지 않습니다.");
            }
        }
        saveToFile(users);
        return modifiedUser;
    }

    @Override
    public boolean deleteById(UUID id){
        try{
            List<User> users = loadFromFile();
            boolean removed = users.removeIf(user -> user.getId().equals(id));
            if(removed) saveToFile(users);
            return removed;
        } catch (NullPointerException e){
            System.out.println("유효하지 않은 ID 입니다..\n" + e);
        }
        return false;
    }
}
