package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.data.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
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

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        return null;
    }

    @Override
    public Optional<BinaryContent> findById() {
        return Optional.empty();
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
