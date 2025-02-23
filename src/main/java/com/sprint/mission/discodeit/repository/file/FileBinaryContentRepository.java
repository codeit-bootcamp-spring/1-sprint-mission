package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BelongType;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path directoryPath;
    private final String FILE_EXTENSION = ".ser";

    private final FileManager fileManager;

    public FileBinaryContentRepository(@Value("${discodeit.repository.file-directory}") String directory, FileManager fileManager) {
        this.fileManager = fileManager;
        this.directoryPath = Path.of(System.getProperty("user.dir"), directory, "binary_contents");
    }

    @PostConstruct
    private void init() {
        fileManager.createDirectory(directoryPath);
    }

    @Override
    public BinaryContent save(BinaryContent content) {
        Path path = directoryPath.resolve(content.getId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(content);
            return content;
        } catch (IOException e) {
            throw new FileIOException("BinaryContent 저장 실패");
        }
    }

    @Override
    public BinaryContent findById(UUID id) {
        Path path = directoryPath.resolve(id.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (BinaryContent) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 BinaryContent 입니다.");
        }
    }

    @Override
    public Optional<BinaryContent> findByUserId(UUID userId) {
        File[] files = directoryPath.toFile().listFiles();
        BinaryContent content = null;

        if (files == null) {
            return Optional.empty();
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                BinaryContent binaryContent = (BinaryContent) ois.readObject();
                if (binaryContent.getType() == BelongType.PROFILE
                        && binaryContent.getBelongTo().equals(userId)) {
                    content = binaryContent;
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new FileIOException("BinaryContent 읽기 실패");
            }
        }

        return Optional.ofNullable(content);
    }

    @Override
    public List<BinaryContent> findByMessageId(UUID messageId) {
        File[] files = directoryPath.toFile().listFiles();
        List<BinaryContent> contents = new ArrayList<>(100);

        if (files == null) {
            return contents;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                BinaryContent binaryContent = (BinaryContent) ois.readObject();
                if (binaryContent.getType() == BelongType.MESSAGE
                        && binaryContent.getBelongTo().equals(messageId)) {
                    contents.add(binaryContent);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new FileIOException("BinaryContent 읽기 실패");
            }
        }
        return contents;
    }

    @Override
    public void delete(UUID id) {
        Path path = directoryPath.resolve(id.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("BinaryContent 삭제 실패");
            }
        }
    }
}
