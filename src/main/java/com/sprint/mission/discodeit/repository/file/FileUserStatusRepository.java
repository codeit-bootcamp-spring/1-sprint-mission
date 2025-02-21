package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// UserService delete 조건 때문에 미리 생성
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path DIRECTORY;
    private final String EXETENSION = ".ser";

    public FileUserStatusRepository(){
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", "UserStatuses-data");
        if(Files.notExists(DIRECTORY)){
            try{
                Files.createDirectories(DIRECTORY);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID userStatusId){
        return DIRECTORY.resolve(userStatusId + EXETENSION);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getId());

        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(userStatus);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        UserStatus userStatusNullable = null;
        Path path = resolvePath(userStatusId);

        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
                    ){
                userStatusNullable = (UserStatus) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(userStatusNullable);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        List<UserStatus> userStatuses = findAll();
        return userStatuses.stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        try{
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXETENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                                ){
                            return (UserStatus) ois.readObject();
                        }
                        catch (IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(UUID userStatusId) {
        Path path = resolvePath(userStatusId);
        try{
            Files.delete(path);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findByUserId(userId)
                .ifPresent(userStatus -> deleteById(userStatus.getId()));
    }
}
