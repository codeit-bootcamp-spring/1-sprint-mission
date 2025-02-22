package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final String filePath;

    public FileBinaryContentRepository(@Value("${file.path.binarycontent}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        UUID fileId = UUID.randomUUID();
        File file = new File(filePath, fileId.toString());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + file.getAbsolutePath(), e);
        }

        return new BinaryContent(fileId, binaryContent.getFileName(), binaryContent.getMimeType(), binaryContent.getFilePath(), binaryContent.getBytes());
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        File file = new File(filePath, id.toString());

        if (!file.exists()) {
            return Optional.empty();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            BinaryContent binaryContent = (BinaryContent) ois.readObject(); // 파일에서 객체 읽기
            return Optional.of(binaryContent);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 읽기 실패: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> binaryContents = new ArrayList<>();
        for (UUID id : ids) {
            findById(id).ifPresent(binaryContents::add);
        }
        return binaryContents;
    }

    @Override
    public boolean existsById(UUID id) {
        return new File(filePath, id.toString()).exists();
    }

    @Override
    public void deleteById(UUID id) {
        File file = new File(filePath, id.toString());
        if (!file.delete()) {
            throw new RuntimeException("파일 삭제 실패: " + file.getAbsolutePath());
        }
    }
}
