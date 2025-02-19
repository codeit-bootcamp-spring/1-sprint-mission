package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceFindDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.validator.BinaryContentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentValidator binaryContentValidator;

    @Override
    public UUID create(BinaryContentCreateDTO dto) {
        binaryContentValidator.validateBinaryContent(dto.getUserId(), dto.getMessageId());

        byte[] file = getFileBytes(dto.getFile());
        String contentType = dto.getFile().getContentType();
        Long size = dto.getFile().getSize();

        BinaryContent binaryContent = new BinaryContent(dto.getUserId(), dto.getMessageId(), file, contentType, size);
        return binaryContentRepository.save(binaryContent);
    }

    public byte[] getFileBytes(MultipartFile multipartFile){
        byte[] file = null;
        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                file =  multipartFile.getBytes();
            } else {
                System.out.println("파일이 존재하지 않거나 비어 있음.");
            }
        } catch (IOException e) {
            System.err.println("파일 변환 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public BinaryContent find(UUID id) {

        BinaryContent findBinaryContent = binaryContentRepository.findOne(id);
        Optional.ofNullable(findBinaryContent)
                .orElseThrow(() -> new NoSuchElementException("해당 BinaryContent 가 없습니다."));
        return findBinaryContent;
    }

    @Override
    public List<BinaryContent> findAll() {
        return binaryContentRepository.findAll();
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids);
    }

    @Override
    public UUID delete(UUID id) {
        return binaryContentRepository.delete(id);
    }
}
