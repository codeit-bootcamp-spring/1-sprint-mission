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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        return List.of();
    }

    @Override
    public void deleteById(UUID id) {

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
