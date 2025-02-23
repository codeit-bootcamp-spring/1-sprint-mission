package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path DIRECTORY;

    public FileBinaryContentRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "BinaryContent.ser");
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createFile(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        try (
                FileOutputStream fos = new FileOutputStream(DIRECTORY.toFile(), true);
                ObjectOutputStream oos = new ObjectOutputStream(fos) {
                    @Override
                    protected void writeStreamHeader() throws IOException {
                        if (fos.getChannel().position() == 0) {
                            super.writeStreamHeader();
                        } else {
                            reset();
                        } //역직렬화 헤더 오류 해결 코드. 파일에 한번만 헤더 들어갈 수 있도록
                    }
                }
        ) {
            oos.writeObject(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        List<BinaryContent> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }

    private List<BinaryContent> readAllContents() {
        List<BinaryContent> contents = new ArrayList<>();
        if (Files.exists(DIRECTORY)) {
            try (
                    FileInputStream fis = new FileInputStream(DIRECTORY.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                while (true) {
                    try {
                        BinaryContent content = (BinaryContent) ois.readObject();
                        contents.add(content);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return contents;
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(UUID id) {
        List<BinaryContent> allContents = readAllContents();
        List<BinaryContent> updatedContents = allContents.stream()
                .filter(content -> !content.getId().equals(id))
                .toList();
        saveAllContents(updatedContents);
    }

    private void saveAllContents(List<BinaryContent> contents) {
        try (
                FileOutputStream fos = new FileOutputStream(DIRECTORY.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            for (BinaryContent content : contents) {
                oos.writeObject(content);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}