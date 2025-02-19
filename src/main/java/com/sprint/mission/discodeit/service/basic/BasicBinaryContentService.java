package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service("basicBinaryContentService")
@Primary
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final Map<UUID, BinaryContent> fileStorage = new HashMap<>();

    @Override
    public Optional<BinaryContentDTO> read(UUID id) {
        return Optional.ofNullable(fileStorage.get(id))
                .map(file -> new BinaryContentDTO(file.getId(), file.getFileName(), file.getData(), file.getOwnerId()));
    }

    @Override
    public List<BinaryContentDTO> readAll() {
        return fileStorage.values().stream()
                .map(file -> new BinaryContentDTO(file.getId(), file.getFileName(), file.getData(), file.getOwnerId()))
                .collect(Collectors.toList());
    }

    @Override
    public Resource download(UUID id) {
        BinaryContent file = fileStorage.get(id);
        if (file == null) {
            throw new RuntimeException("❌ 파일을 찾을 수 없습니다: " + id);
        }
        return new ByteArrayResource(file.getData());  // ✅ 바이너리 데이터를 그대로 반환
    }

    @Override
    public UUID upload(MultipartFile file, UUID ownerId) {
        try {
            UUID fileId = UUID.randomUUID();
            BinaryContent binaryContent = new BinaryContent(fileId, file.getOriginalFilename(), file.getBytes(), ownerId);
            fileStorage.put(fileId, binaryContent);
            return fileId;
        } catch (IOException e) {
            throw new RuntimeException("❌ 파일 저장 실패: " + e.getMessage());
        }
    }

    // ✅ 파일 삭제 기능 추가 (오류 해결)
    @Override
    public void delete(UUID id) {
        if (fileStorage.containsKey(id)) {
            fileStorage.remove(id);
        } else {
            throw new RuntimeException("❌ 삭제할 파일을 찾을 수 없습니다: " + id);
        }
    }
}
