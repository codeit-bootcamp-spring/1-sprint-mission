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
        List<BinaryContent> binaryContents = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORY.toFile()))) {
            while (true) {
                try {
                    BinaryContent existingBinaryContent = (BinaryContent) ois.readObject();
                    binaryContents.add(existingBinaryContent);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            // 파일이 없는 경우, 새 파일 생성 후 저장
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 이진 콘텐츠 업데이트 또는 추가
        boolean binaryContentUpdated = false;
        for (int i = 0; i < binaryContents.size(); i++) {
            if (binaryContents.get(i).getId().equals(binaryContent.getId())) {
                binaryContents.set(i, binaryContent);
                binaryContentUpdated = true;
                break;
            }
        }
        if (!binaryContentUpdated) {
            binaryContents.add(binaryContent);
        }

        // 파일에 다시 쓰기
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORY.toFile()))) {
            for (BinaryContent bc : binaryContents) {
                oos.writeObject(bc);
            }
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