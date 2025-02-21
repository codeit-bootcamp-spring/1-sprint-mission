package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileReadStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"),"file-data-map", ReadStatus.class.getSimpleName());
        if(Files.notExists(DIRECTORY)){
            try{
                Files.createDirectories(DIRECTORY);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
    public Path resolvePath(UUID readStatusId) {
        return DIRECTORY.resolve(readStatusId+EXTENSION);
    }
    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path path = resolvePath(readStatus.getId());
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                ){
            oos.writeObject(readStatus);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return readStatus;
    }

    @Override
    public void saveAll(List<ReadStatus> readStatuses) {
        for(ReadStatus readStatus : readStatuses){
            save(readStatus);
        }
    }

    @Override
    public Optional<ReadStatus> findById(UUID readStatusId) {
        ReadStatus readStatus = null;
        Path path = resolvePath(readStatusId);
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ){
                readStatus = (ReadStatus) ois.readObject();
            } catch (IOException|ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(readStatus);
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return findAll().stream()
                .filter(r->r.getUser().getId().equals(userId) && r.getChannel().getId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return findAll().stream()
                .filter(r->r.getUser().getId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAll() {
        try{
            return Files.list(DIRECTORY)
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path->{
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                                ){
                            return (ReadStatus) ois.readObject();
                        }catch (IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        try{
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }
}
