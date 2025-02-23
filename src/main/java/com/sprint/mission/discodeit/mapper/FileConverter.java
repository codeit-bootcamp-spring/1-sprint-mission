package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class FileConverter {
    public Optional<BinaryContentCreateRequest> convertToBinaryRequest(MultipartFile file) {

        try {
            if (file == null) {
                log.error("Profile image conversion fail : file == null");
                return Optional.empty();
            }
            if (file.isEmpty()) {
                log.error("file is empty.");
                return Optional.empty();
            }

            return Optional.of(new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            ));
        } catch (IOException e) {
            log.error("파일 변환 실패 : {}", file.getOriginalFilename(), e);
            return Optional.empty();
        }
    }
}
