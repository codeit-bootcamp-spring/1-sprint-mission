package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    // 파일 생성
    @Override
    public BinaryContent create(CreateBinaryContentRequestDto createBinaryContentRequestDto) throws IOException {

        MultipartFile multipartFile = createBinaryContentRequestDto.multipartFile();
        String fileName = createBinaryContentRequestDto.filePath();

        BinaryContent binaryContent = new BinaryContent(multipartFile, fileName);

        binaryContentRepository.save(binaryContent);

        return binaryContent;
    }

    // 파일 단건 조회
    @Override
    public BinaryContentDto find(UUID id) {

        BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 파일이 존재하지 않습니다."));

        return BinaryContentMapper.INSTANCE.toDto(binaryContent);
    }

    // 파일 다건 조회
    @Override
    public List<BinaryContentDto> findAll() {

        List<BinaryContentDto> list= binaryContentRepository.findAll().stream()
                .map(BinaryContentMapper.INSTANCE::toDto)
                .toList();

        if (list.isEmpty()) {
            throw new NoSuchElementException("파일이 존재하지 않습니다.");
        }

        return list;
    }

    // 파일 다건 조회 - id list로 반환
    @Override
    public List<UUID> findAllByIdIn() {

        List<UUID> list = binaryContentRepository.findAll().stream()
                .map(BinaryContent::getId)
                .toList();

        if (list.isEmpty()) {
            throw new NoSuchElementException("파일이 존재하지 않습니다.");
        }

        return list;
    }

    // 파일 삭제
    @Override
    public void delete(UUID id) {
        
        BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow();
        binaryContent.deleteFile();     // 로컬에 저장된 사진 파일 삭제

        binaryContentRepository.deleteById(id); // 저장된 객체 삭제
    }
}
