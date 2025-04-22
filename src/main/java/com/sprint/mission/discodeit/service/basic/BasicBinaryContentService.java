package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.binarycontent.EmptyFileUploadException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Transactional
    @Override
    public BinaryContentResponseDto create(MultipartFile file) throws IOException {
        log.debug("파일 업로드(생성) 요청 - 원본 파일명: {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new EmptyFileUploadException(file.getOriginalFilename());
        }
        BinaryContent savedFile = binaryContentRepository.save(binaryContentMapper.toEntity(file));
        binaryContentStorage.put(savedFile.getId(), file);

        log.info("파일 업로드를 성공하였습니다. - id: {}, 파일명: {}, 크기: {} bytes", savedFile.getId(), file.getOriginalFilename(), file.getSize());
        return binaryContentMapper.toResponseDto(binaryContentRepository.findById(savedFile.getId()).orElseThrow(() -> new BinaryContentNotFoundException(savedFile.getId())));
    }

    @Override
    public BinaryContentResponseDto find(UUID fileId) {
        log.debug("파일 조회 요청 - id: {}", fileId);
        BinaryContent file = binaryContentRepository.findById(fileId)
                .orElseThrow(() -> new BinaryContentNotFoundException(fileId));

        log.info("파일 조회에 성공하였습니다. - id: {}", fileId);
        return binaryContentMapper.toResponseDto(file);
    }

    @Override
    public List<BinaryContentResponseDto> findAll() {
        log.debug("전체 파일 목록 조회 요청");
        List<BinaryContentResponseDto> result = binaryContentRepository.findAll().stream()
                .map(binaryContentMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("전체 파일 목록 조회를 완료하였습니다. - 전체 파일 개수: {}", result.size());
        return result;
    }

    @Transactional
    @Override
    public BinaryContentResponseDto delete(UUID fileId) {
        log.debug("파일 삭제 요청 - 파일 id: {}", fileId);
        BinaryContent deletedFile = binaryContentRepository.findById(fileId).orElseThrow(() -> new BinaryContentNotFoundException(fileId));
        binaryContentRepository.delete(deletedFile);

        log.info("파일 데이터가 삭제되었습니다. - id: {}", deletedFile.getId());
        return binaryContentMapper.toResponseDto(deletedFile);
    }

    @Override
    public ResponseEntity<?> download(UUID fileId) {
        log.debug("파일 다운로드 요청 - 파일 id: {}", fileId);
        BinaryContent file = binaryContentRepository.findById(fileId).orElseThrow(() -> new BinaryContentNotFoundException(fileId));

        log.info("파일 다운로드를 성공하였습니다. - id: {}", fileId);
        return binaryContentStorage.download(binaryContentMapper.toResponseDto(file));
    }
}
