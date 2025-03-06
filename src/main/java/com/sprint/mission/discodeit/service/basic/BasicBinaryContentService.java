package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.FindBinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

        binaryContentIsExist(id);
        BinaryContent binaryContent = binaryContentRepository.load().get(id);

        return FindBinaryContentResponseDto.fromEntity(binaryContent);
    }

    @Override
    public List<FindBinaryContentResponseDto> findAll() {

        List<FindBinaryContentResponseDto> list = binaryContentRepository.load().values().stream()
                .map(FindBinaryContentResponseDto::fromEntity)
                .toList();

        if (list.isEmpty()) {
            throw new NoSuchElementException("파일이 존재하지 않습니다.");
        }

        return list;
    }

    @Override
    public List<UUID> findAllByIdIn() {

        List<UUID> list = binaryContentRepository.load().values().stream()
                .map(BinaryContent::getId)
                .toList();

        if (list.isEmpty()) {
            throw new NoSuchElementException("파일이 존재하지 않습니다.");
        }

        return list;
    }

    @Override
    public void delete(UUID id) {
        
        BinaryContent binaryContent = binaryContentRepository.load().get(id);
        binaryContent.deleteFile();     // 로컬에 저장된 사진 파일 삭제

        binaryContentRepository.delete(id); // 저장된 객체 삭제
    }

    private void binaryContentIsExist(UUID id) {
        Map<UUID, BinaryContent> list = binaryContentRepository.load();

        if (!list.containsKey(id)) {
            throw new NoSuchElementException("해당 파일이 존재하지 않습니다.");
        }
    }
}
