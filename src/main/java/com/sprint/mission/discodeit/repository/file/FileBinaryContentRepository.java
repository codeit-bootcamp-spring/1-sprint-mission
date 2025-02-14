package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.data.BinaryContent;
import com.sprint.mission.discodeit.entity.data.ContentType;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path DIRECTORY;
    private final Path PROFILE_IMAGE;
    private final Path MESSAGE_IMAGE;
    private final String EXTENSION = ".ser";

    public FileBinaryContentRepository(){
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"),
                "file-data-map", BinaryContent.class.getSimpleName());
        this.PROFILE_IMAGE = Paths.get(System.getProperty("user.dir"),
                "file-data-map", BinaryContent.class.getSimpleName(),"PROFILE_IMAGE");
        this.MESSAGE_IMAGE = Paths.get(System.getProperty("user.dir"),
                "file-data-map", BinaryContent.class.getSimpleName(),"MESSAGE_IMAGE");
        init(DIRECTORY);
        init(PROFILE_IMAGE);
        init(MESSAGE_IMAGE);
    }

    private Path resolvePath(UUID id, ContentType contentType){
        return DIRECTORY.resolve(contentType.name()).resolve(id + EXTENSION);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent, ContentType contentType) {
        Path path = resolvePath(binaryContent.getId(), contentType);
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return binaryContent;
    }
    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Path[] directories = {PROFILE_IMAGE, MESSAGE_IMAGE};

        for (Path dir : directories) {
            Optional<BinaryContent> content = findInDirectory(dir, id);
            if (content.isPresent()) {
                return content;
            }
        }
        return Optional.empty();
    }

    private Optional<BinaryContent> findInDirectory(Path directory, UUID id) {
        Path path = directory.resolve(id + EXTENSION);

        if (!Files.exists(path)) {
            return Optional.empty();
        }

        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return Optional.ofNullable((BinaryContent) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 읽기 오류 발생: " + path, e);
        }
    }


    @Override
    public List<BinaryContent> findAll() {
            List<BinaryContent> allContents = new ArrayList<>();
            allContents.addAll(findFilesInDirectory(PROFILE_IMAGE));
            allContents.addAll(findFilesInDirectory(MESSAGE_IMAGE));
            return allContents;
    }

    private List<BinaryContent> findFilesInDirectory(Path directory) {
        List<BinaryContent> contents = new ArrayList<>();
        try {
            Files.list(directory)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .forEach(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            BinaryContent content = (BinaryContent) ois.readObject();
                            contents.add(content);
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("파일 읽기 오류 발생: " + path, e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 순회 오류 발생", e);
        }
        return contents;
    }

    @Override
    public void deleteById(UUID id, ContentType contentType) {
        Path path = resolvePath(id, contentType);
        try {
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void init(Path DIRECTORY) {
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
