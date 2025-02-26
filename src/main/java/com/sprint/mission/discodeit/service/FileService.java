package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.FileDTO;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.UUID;

public interface FileService {
    Resource downloadFile(UUID fileId);  // ✅ 파일 다운로드
    List<FileDTO> getFiles(List<UUID> fileIds);  // ✅ 여러 개 파일 조회
    void deleteFile(UUID fileId);  // ✅ 파일 삭제 기능 추가
}
