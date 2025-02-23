package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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

    public BinaryContent create(BinaryContentRequest request) {

        //존재하는 user or message에 대한 요청인지 확인
        BinaryContent.Type type = request.type();
        if (type == BinaryContent.Type.PROFILE) {
            userRepository.findById(request.belongTo())
                    .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + request.belongTo()));
        } else {
            messageRepository.findById(request.belongTo())
                    .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + request.belongTo()));
        }
        MultipartFile file = request.file();

        UUID id = UUID.randomUUID();
        String fileName = generateFileName(id, file.getOriginalFilename());
        Path path = directoryPath.resolve(fileName);

        try {
            file.transferTo(path);
        } catch (IOException e) {
            throw new FileIOException("파일 생성 실패");
        }

        BinaryContent content = BinaryContent.of(id, request.type(),
                request.belongTo(), fileName, path.toString());
        return binaryContentRepository.save(content);
    }

    private String generateFileName(UUID id, String originalName) {
        String extension = originalName.substring(originalName.indexOf("."));
        return id + extension;
    }

    public Path find(UUID id) {
        BinaryContent content = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 binary content. id=" + id));
        return Path.of(content.getPath());
    }

    public Path findByUserId(UUID userId) {
        BinaryContent content = binaryContentRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 user에 대한 BinaryContent 요청"));
        return Path.of(content.getPath());
    }

    public List<Path> findByMessageId(UUID messageId) {
        ArrayList<Path> paths = new ArrayList<>(100);
        List<BinaryContent> contents = binaryContentRepository.findByMessageId(messageId);
        for (BinaryContent content : contents) {
            paths.add(Path.of(content.getPath()));
        }
        return paths;
    }

    public void delete(UUID id) {
        BinaryContent content = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 binary content. id=" + id));
        fileManager.deleteFile(Path.of(content.getPath()));
        binaryContentRepository.delete(id);
    }
}
