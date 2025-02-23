package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDto.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.binaryContentDto.FindBinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UUID create(CreateBinaryContentRequestDto createBinaryContentRequestDto) throws IOException {

        MultipartFile multipartFile = createBinaryContentRequestDto.multipartFile();
        String fileName = createBinaryContentRequestDto.filePath();

        BinaryContent binaryContent = new BinaryContent(multipartFile, fileName);

        binaryContentRepository.save(binaryContent);

        return binaryContent.getId();
    }

    @Override
    public FindBinaryContentResponseDto find(UUID id) {

        BinaryContent binaryContent = binaryContentRepository.load().get(id);

        return FindBinaryContentResponseDto.fromEntity(binaryContent);
    }

    @Override
    public List<FindBinaryContentResponseDto> findAll() {

        return binaryContentRepository.load().values().stream()
                .map(FindBinaryContentResponseDto::fromEntity)
                .toList();
    }

    @Override
    public List<UUID> findAllByIdIn() {

        return binaryContentRepository.load().values().stream()
                .map(BinaryContent::getId)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        
        BinaryContent binaryContent = binaryContentRepository.load().get(id);
        binaryContent.deleteFile();     // 로컬에 저장된 사진 파일 삭제

        binaryContentRepository.delete(id); // 저장된 객체 삭제
    }
}
