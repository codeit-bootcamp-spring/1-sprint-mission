package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private static final String STORAGE_DIR = "tmp/storage";


    public FileBinaryContentRepository() {
        try {
            Files.createDirectories(Paths.get(STORAGE_DIR));
        }catch (IOException e){
            throw new RuntimeException("Failed to create storage directory" , e);
        }
    }

    @Override
    public void save(BinaryContent binaryContent) {
        Path filePath = getFilePath(binaryContent.getId());
        try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(binaryContent);
        }catch (IOException e){
            throw new RuntimeException("Failed to save binary content" , e);
        }
    }

    @Override
    public List<BinaryContent> findAllByUserId(UUID userId) {
        return findAll().stream()
                .filter(content -> userId.equals(content.getUserId()))
                .toList();
    }

    @Override
    public List<BinaryContent> findAllByMessageId(UUID messageId) {
        return findAll().stream()
                .filter(content -> messageId.equals(content.getMessageId()))
                .toList();
    }

    @Override
    public Optional<BinaryContent> findById(UUID binaryContentId) {
        Path filePath = getFilePath(binaryContentId);
        if(!Files.exists(filePath)){
            return Optional.empty();
        }

        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return Optional.of((BinaryContent)ois.readObject());
        }catch (IOException | ClassNotFoundException e){
            throw new RuntimeException("Failed to read binary content" , e);
        }
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return findAll().stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }

    @Override
    public void delete(BinaryContent binaryContent) {
        deleteFile(getFilePath(binaryContent.getId()));
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        List<Path> filesToDelete = findAll().stream()
                .filter(content -> messageId.equals(content.getMessageId()))
                .map(content -> getFilePath(content.getId()))
                .toList();

        filesToDelete.forEach(this::deleteFile);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        List<Path> filesToDelete = findAll().stream()
                .filter(content -> userId.equals(content.getUserId()))
                .map(content -> getFilePath(content.getId()))
                .toList();

        filesToDelete.forEach(this::deleteFile);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return findAll().stream()
                .anyMatch(content -> userId.equals(content.getUserId()));
    }

    private Path getFilePath(UUID id) {
        return Paths.get(STORAGE_DIR, id.toString() + ".bin");
    }

    //파일을 BinaryContent로 변환하는 메서드
    private BinaryContent deserializeBinaryContent(Path filePath){
        if(!Files.exists(filePath)){
            return null;
        }
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (BinaryContent) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            return null;
        }
    }

    private List<BinaryContent> findAll(){
        try(Stream<Path> files = Files.list(Paths.get(STORAGE_DIR))) {
            return files.map(this::deserializeBinaryContent)
                        .filter(Objects::nonNull)
                        .toList();
        }catch (IOException e){
            throw new RuntimeException("Failed to find files" , e);
        }
    }

    private void deleteFile(Path filePath){
        try {
            Files.deleteIfExists(filePath);
        }catch (IOException e){
            throw new RuntimeException("Failed to delete file: " + filePath , e);
        }
    }

}
