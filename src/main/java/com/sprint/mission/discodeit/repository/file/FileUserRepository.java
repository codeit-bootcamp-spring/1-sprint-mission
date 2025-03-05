package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileUserRepository implements UserRepository {
    private final String filePath = "users.dat";

    @Override
    public User save(User user) {
        Map<String, User> users = readFromFile();
        users.put(user.getId(), user);
        writeToFile(users);
        return user;
    }

    @Override
    public void deleteById(String id) {
        Map<String, User> users = readFromFile();
        users.remove(id);
        writeToFile(users);
    }

    @Override
    public Optional<User> findById(String id) {
        Map<String, User> users = readFromFile();
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<UsersDto> findAll() {
        return readFromFile().values().stream()
                .map(this::convertToUsersDTO)
                .collect(Collectors.toList());
    }
    private UsersDto convertToUsersDTO(User user) {
        UsersDto dto = new UsersDto();
        dto.setId(user.getId());
        dto.setName(user.getName());

        if (user.getEmail() != null) {
            dto.setEmail(user.getEmail());
        }

        dto.setOnline(user.isOnline());

        if (user.getProfileImage() != null && user.getProfileImage().length > 0) {
            try {
                String base64Image = Base64.getEncoder().encodeToString(user.getProfileImage());
                dto.setProfileImage(base64Image);
            } catch (Exception e) {
                System.err.println("이미지 인코딩 중 오류: " + e.getMessage());
            }
        }

        return dto;
    }

    private Map<String, User> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void writeToFile(Map<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}