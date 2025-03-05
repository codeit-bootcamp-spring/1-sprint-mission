package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class FileUserStatusRepository {
    public void saveStatus(UUID uid) throws IOException, ClassNotFoundException {
        UserStatus userStatus = new UserStatus();
        userStatus.setId(uid);
        userStatus.setCreatedAt(Instant.now());
        userStatus.setUpdatedAt(Instant.now());
        userStatus.setUserOnOff(true);

        saveStatus(userStatus);
    }

    public void updateStatus(UserDto userDto) throws IOException, ClassNotFoundException {
        List<UserStatus> statuses = getUserStatusListFromFile();

        for(UserStatus userStatus : statuses){
            if(userStatus.getId().equals(userDto.getId())){
                userStatus.setUpdatedAt(Instant.now());
                userStatus.setUserOnOff(!userStatus.isUserOnOff());
                modifyUserStatusOfFile(statuses);
                return;
            }
        }
    }

    public void delete(UUID uid) throws IOException, ClassNotFoundException {
        List<UserStatus> statuses = getUserStatusListFromFile();
        statuses.removeIf(status -> status.getId().equals(uid));
        modifyUserStatusOfFile(statuses);
    }

    public void checkOnline(UUID uid) throws IOException, ClassNotFoundException {
        List<UserStatus> statuses = getUserStatusListFromFile();
        for(UserStatus userStatus : statuses){
            if(userStatus.getId().equals(uid)){
                if(userStatus.getUpdatedAt().plus(5, ChronoUnit.MINUTES).compareTo(Instant.now()) > 0){
                    userStatus.setUserOnOff(false);
                    modifyUserStatusOfFile(statuses);
                }
            }
        }
    }

    public boolean find(UserDto user) throws IOException, ClassNotFoundException {
        List<UserStatus> statuses = getUserStatusListFromFile();
        if(statuses.isEmpty()) {
            return false;
        }

        for(UserStatus status : statuses){
            if(status.getId().equals(user.getId())){
                return status.isUserOnOff();
            }
        }

        throw new IOException();
    }

    public UserStatus findByUserId(UserDto user) throws IOException, ClassNotFoundException {
        List<UserStatus> statuses = getUserStatusListFromFile();
        for(UserStatus status : statuses){
            if(status.getId().equals(user.getId())){
                return status;
            }
        }

        return null;
    }

    public List<UserStatus> findAll() throws IOException, ClassNotFoundException {
        return getUserStatusListFromFile();
    }

    private void saveStatus(UserStatus readStatus) throws IOException, ClassNotFoundException {
        List<UserStatus> userStatuses = getUserStatusListFromFile();
        userStatuses.add(readStatus);

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("UserStatus.ser"));
            out.writeObject(userStatuses);
            out.close();
        }
        catch (IOException ignored) {}
    }

    private List<UserStatus> getUserStatusListFromFile() throws IOException, ClassNotFoundException {
        List<UserStatus> userStatuses = new ArrayList<>();

        File file = new File("UserStatus.ser");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                userStatuses = (List<UserStatus>) in.readObject();
                in.close();
            }
        }

        return userStatuses;
    }

    private void modifyUserStatusOfFile(List<UserStatus> readStatuses) throws IOException {
        try (
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("UserStatus.ser"))) {
            out.writeObject(readStatuses);
            out.close();
        }
    }
}
