package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BelongType;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
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

    private final String directory = "files";
    private final FileManager fileManager = new FileManager(directory);

    public BinaryContent create(BinaryContentDto dto) {

        //존재하는 user or message에 대한 요청인지 확인
        BelongType type = dto.getType();
        if (type == BelongType.PROFILE) {
            userRepository.findById(dto.getBelongTo());
        } else {
            messageRepository.findById(dto.getBelongTo());
        }

        UUID id = UUID.randomUUID();
        byte[] data = dto.getData();
        String fileName = generateFileName(id, dto.getName());
        Path path = fileManager.getPath().resolve(fileName);
        fileManager.createFile(fileName, data);

        BinaryContent content = BinaryContent
                .of(id, dto.getType(), dto.getBelongTo(), fileName, path.toString());
        return binaryContentRepository.save(content);
    }

    public void update(UUID id, BinaryContentDto dto) {
        BinaryContent content = binaryContentRepository.findById(id);
        fileManager.deleteFile(content.getName());

        String fileName = generateFileName(id, dto.getName());
        fileManager.createFile(fileName, dto.getData());
        content.setName(fileName);
        content.setPath(fileManager.getPath().resolve(fileName).toString());
        binaryContentRepository.save(content);
    }

    private String generateFileName(UUID id, String originalName) {
        String extension = originalName.substring(originalName.indexOf("."));
        return id + extension;
    }

    public BinaryContentDto find(UUID id) {
        BinaryContent content = binaryContentRepository.findById(id);
        byte[] data = fileManager.readFile(content.getName());
        return BinaryContentDto.of(content.getName(), content.getType(), content.getId(), data);
    }

    public BinaryContentDto findByUserId(UUID userId) {
        Optional<BinaryContent> binaryContent = binaryContentRepository.findByUserId(userId);
        BinaryContent content = binaryContent.orElseThrow(
                () -> new NotFoundException("존재하지 않는 user에 대한 BinaryContent 요청"));
        byte[] data = fileManager.readFile(content.getName());
        return BinaryContentDto.of(content.getName(), content.getType(), content.getBelongTo(), data);
    }

    public List<BinaryContentDto> findByMessageId(UUID messageId) {
        ArrayList<BinaryContentDto> dtos = new ArrayList<>(100);
        List<BinaryContent> contents = binaryContentRepository.findByMessageId(messageId);
        for (BinaryContent content : contents) {
            byte[] data = fileManager.readFile(content.getName());
            BinaryContentDto dto = BinaryContentDto.of(content.getName(), content.getType(), content.getBelongTo(), data);
            dtos.add(dto);
        }
        return dtos;
    }

    public void delete(UUID id) {
        BinaryContent content = binaryContentRepository.findById(id);
        fileManager.deleteFile(content.getName());
        binaryContentRepository.delete(id);
    }
}
