package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileUserRepository {

    private final FileBinaryContentRepository binaryContentRepository;
    private final FileUserStatusRepository statusRepository;

    @Autowired
    public FileUserRepository(FileBinaryContentRepository binaryContentRepository, FileUserStatusRepository statusRepository) {
        this.binaryContentRepository = binaryContentRepository;
        this.statusRepository = statusRepository;
    }

    public void createUser(UserDto userDto) throws IOException, ClassNotFoundException {
        User user = new User();
        List<User> users = getUserListFromFile();

        for(User u : users){
            if(u.getName().equals(user.getName())){
                throw new IOException();
            }
        }
        user.setName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        if(!userDto.getEmail().matches(emailRegex)) throw new IOException();

        user.setEmail(userDto.getEmail());

        for(User u : users){
            if(u.getId().equals(user.getId())){
                while(!u.getId().equals(user.getId())){
                    user.setId(UUID.randomUUID());
                }
            }
        }

        user.setIsContainContent(userDto.getContainContent());

        saveUser(user);
    }

    public boolean update(UUID id, String newNickname) throws IOException, ClassNotFoundException {
        List<User> users = getUserListFromFile();
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

        modifyUserOfFile(users);
        return true;
    }

    public boolean checkUserId(UUID id) throws IOException, ClassNotFoundException {
        List<User> users = getUserListFromFile();
        for(User user : users){
            if(user.getId().equals(id)){
                return true;
            }
        }

        return false;
    }

    public void deleteUser(UUID id) throws IOException, ClassNotFoundException {
        List<User> users = getUserListFromFile();
        for(User user : users){
            if(user.getId().equals(id)){
                users.remove(user);
                binaryContentRepository.deleteUser(id);
                break;
            }
        }
        modifyUserOfFile(users);
    }

    public User find(UserDto userDto) throws IOException, ClassNotFoundException {
        List<User> users = getUserListFromFile();

        for(User user : users){
            if(user.getName().equals(userDto.getUserName()) && user.getPassword().equals(userDto.getPassword())){
                return user;
            }
        }

        return null;
    }

    public void findAll() throws IOException, ClassNotFoundException {
        List<User> users;

        if(getUserListFromFile() == null){
            users = new ArrayList<>();
        }
        else users = getUserListFromFile();

        for(User user : users){
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            System.out.println(user + "status : " + statusRepository.find(userDto));
        }
    }

    private void saveUser(User user) throws IOException, ClassNotFoundException {
        List<User> users = getUserListFromFile();
        users.add(user);


        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("User.ser"));
            out.writeObject(users);
            out.close();
        }
        catch (IOException ignored) {}
    }


    private List<User> getUserListFromFile() throws IOException, ClassNotFoundException {
        List<User> users = new ArrayList<>();


        File file = new File("User.ser");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                users = (List<User>) in.readObject();
            }
        }

        return users;
    }

    public User getUser(User user) throws IOException, ClassNotFoundException {
        List<User> user1 = getUserListFromFile();
        for(User user2 : user1){
            if(user2.getId().equals(user.getId())){
                return user2;
            }
        }

        System.out.println("해당하는 유저가 없습니다.");
        return null;
    }

    private void modifyUserOfFile(List<User> users) throws IOException {
        try (
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("User.ser"))) {
                out.writeObject(users);
        }
    }
}
