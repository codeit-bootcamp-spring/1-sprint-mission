package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentService {
    Optional<BinaryContentDTO> read(UUID id);  // ✅ 파일 읽기
    List<BinaryContentDTO> readAll();  // ✅ 모든 파일 조회
    Resource download(UUID id);  // ✅ 파일 다운로드
    UUID upload(MultipartFile file, UUID ownerId);  // ✅ 파일 업로드

    // ✅ 파일 삭제 기능 추가
    void delete(UUID id);
}
