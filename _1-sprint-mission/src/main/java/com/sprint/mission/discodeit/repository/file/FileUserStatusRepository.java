package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"),"file-data-map",UserStatus.class.getSimpleName());
        if(Files.notExists(DIRECTORY)){
            try{
                Files.createDirectories(DIRECTORY);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
    public Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id+EXTENSION);
    }
    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getId());
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(userStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        UserStatus userStatus = null;
        Path path = resolvePath(userStatusId);
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ){
                userStatus = (UserStatus) ois.readObject();
            }catch (IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(userStatus);
    }

    @Override
    public List<UserStatus> findAll() {
        try{
            return Files.list(DIRECTORY)
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path->{
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                                ){
                            return (UserStatus)ois.readObject();
                        }catch (IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    }).toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(UUID userStatusId) {
        Path path = resolvePath(userStatusId);
        try {
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(UUID userStatusId) {
        Path path = resolvePath(userStatusId);
        return Files.exists(path);
    }
}
