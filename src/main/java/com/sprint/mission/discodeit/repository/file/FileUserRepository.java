package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileService;

import java.io.*;
import java.util.*;

public class FileUserService implements FileService<User> {
    private static final String USER_SAVE_FILE = "config/user.ser";

    @Override
    public List<User> readFromFile() {
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
    public void writeToFile(List<User> data) {
        try (FileOutputStream fos = new FileOutputStream(USER_SAVE_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(data);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
