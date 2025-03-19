package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Transactional
    @Override
    public BinaryContentResponseDto create(MultipartFile file) throws IOException {
        BinaryContent savedFile = binaryContentRepository.save(binaryContentMapper.toEntity(file));
        binaryContentStorage.put(savedFile.getId(), file);
        return binaryContentMapper.toResponseDto(binaryContentRepository.findById(savedFile.getId()).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA)));
    }

    @Override
    public BinaryContentResponseDto find(UUID fileId) {
        return binaryContentMapper.toResponseDto(binaryContentRepository.findById(fileId).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA)));
    }

    @Override
    public List<BinaryContentResponseDto> findAll() {
        return binaryContentRepository.findAll().stream()
                .map(binaryContentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BinaryContentResponseDto delete(UUID fileId) {
        BinaryContent deletedFile = binaryContentRepository.findById(fileId).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
        binaryContentRepository.delete(deletedFile);
        return binaryContentMapper.toResponseDto(deletedFile);
    }

    @Override
    public ResponseEntity<?> download(UUID fileId) {
        BinaryContent file = binaryContentRepository.findById(fileId).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
        return binaryContentStorage.download(binaryContentMapper.toResponseDto(file));
    }
}
