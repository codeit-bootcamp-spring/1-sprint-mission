package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final FileManager fileManager;

    private final Path directoryPath = Path.of(System.getProperty("user.dir"), "files");

    @PostConstruct
    public void init() {
        fileManager.createDirectory(directoryPath);
    }

    public BinaryContent create(BinaryContentRequest dto) {

        //존재하는 user or message에 대한 요청인지 확인
        BinaryContent.Type type = dto.type();
        if (type == BinaryContent.Type.PROFILE) {
            userRepository.findById(dto.belongTo())
                    .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + dto.belongTo()));
        } else {
            messageRepository.findById(dto.belongTo())
                    .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + dto.belongTo()));
        }

        UUID id = UUID.randomUUID();
        byte[] data = dto.data();
        String fileName = generateFileName(id, dto.name());
        Path path = directoryPath.resolve(fileName);
        fileManager.createFile(path, data);

        BinaryContent content = BinaryContent.of(id, dto.type(),
                dto.belongTo(), fileName, path.toString());
        return binaryContentRepository.save(content);
    }

    private String generateFileName(UUID id, String originalName) {
        String extension = originalName.substring(originalName.indexOf("."));
        return id + extension;
    }

    public BinaryContentRequest find(UUID id) {
        BinaryContent content = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 binary content. id=" + id));
        byte[] data = fileManager.readFile(Path.of(content.getPath()));
        return BinaryContentRequest.of(content.getName(), content.getType(),
                content.getId(), data);
    }

    public BinaryContentRequest findByUserId(UUID userId) {
        Optional<BinaryContent> binaryContent = binaryContentRepository.findByUserId(userId);
        BinaryContent content = binaryContent.orElseThrow(
                () -> new NotFoundException("존재하지 않는 user에 대한 BinaryContent 요청"));
        byte[] data = fileManager.readFile(Path.of(content.getPath()));
        return BinaryContentRequest.of(content.getName(), content.getType(),
                content.getBelongTo(), data);
    }

    public List<BinaryContentRequest> findByMessageId(UUID messageId) {
        ArrayList<BinaryContentRequest> dtos = new ArrayList<>(100);
        List<BinaryContent> contents = binaryContentRepository.findByMessageId(messageId);
        for (BinaryContent content : contents) {
            byte[] data = fileManager.readFile(Path.of(content.getPath()));
            BinaryContentRequest dto = BinaryContentRequest.of(content.getName(),
                    content.getType(), content.getBelongTo(), data);
            dtos.add(dto);
        }
        return dtos;
    }

    public void delete(UUID id) {
        BinaryContent content = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 binary content. id=" + id));
        fileManager.deleteFile(Path.of(content.getPath()));
        binaryContentRepository.delete(id);
    }
}
