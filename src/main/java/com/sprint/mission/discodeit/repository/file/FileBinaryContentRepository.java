package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BelongType;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Primary
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final String directory = "binary_contents";
    private final String FILE_EXTENSION = ".ser";

    private final FileManager fileManager = new FileManager(directory);
    private final Path filePath = fileManager.getPath();

    @Override
    public BinaryContent save(BinaryContent content) {
        Path path = filePath.resolve(content.getId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(content);
            return content;
        } catch (IOException e) {
            throw new FileIOException("BinaryContent 저장 실패");
        }
    }

    @Override
    public BinaryContent findById(UUID id) {
        Path path = filePath.resolve(id.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (BinaryContent) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 BinaryContent 입니다.");
        }
    }

    @Override
    public Optional<BinaryContent> findByUserId(UUID userId) {
        File[] files = filePath.toFile().listFiles();
        BinaryContent content = null;

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
        File[] files = filePath.toFile().listFiles();
        List<BinaryContent> contents = new ArrayList<>(100);

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

        if (contents.isEmpty()) {
            throw new NotFoundException("존재하지 않는 message에 대한 BinaryContent 요청.");
        }
        return contents;
    }

    @Override
    public void delete(UUID id) {
        Path path = filePath.resolve(id.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("BinaryContent 삭제 실패");
            }
        }
    }
}
