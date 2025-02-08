package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;

    public BinaryContentResponseDTO create(BinaryContentCreateDTO createDTO){
        // 관련된 유저 확인
        User user = userRepository.findById(createDTO.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //새로운 binaryContent 생성, 저장
        BinaryContent binaryContent = new BinaryContent(
                UUID.randomUUID(),
                user,
                createDTO.filename(),
                createDTO.contentType(),
                createDTO.fileData()
        );
        binaryContentRepository.save(binaryContent);

        // DTO 변환 반환
        return new BinaryContentResponseDTO(
                binaryContent.getId(),
                binaryContent.getUser().getId(),
                binaryContent.getFilename(),
                binaryContent.getContentType(),
                binaryContent.getCreatedAt()
        );
    }

    public BinaryContentResponseDTO find(UUID binaryContentId){
        // ID 기준으로 조회
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new IllegalArgumentException("BinaryContent not found"));
        //DTO 변환 반환
        return BinaryContentResponseDTO.fromEntity(binaryContent);
    }

    public List<BinaryContentResponseDTO> findAllByIdIn(List<UUID> binaryContentIds){
        // id 목록 기반으로 조회
        List<BinaryContent> binaryContents = binaryContentRepository.findAllByIdIn(binaryContentIds);
        //조회된 데이터 DTO 변환 반환
        return binaryContents.stream()
                .map(BinaryContentResponseDTO::fromEntity)
                .toList();
    }

    public void delete(UUID binaryContentId){
        // id 기준 으로 조회
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new IllegalArgumentException("BinaryContent not found"));
        //파일 삭제
        binaryContentRepository.delete(binaryContent);

        System.out.println("BinaryContent deleted: " + binaryContentId);

    }
}
