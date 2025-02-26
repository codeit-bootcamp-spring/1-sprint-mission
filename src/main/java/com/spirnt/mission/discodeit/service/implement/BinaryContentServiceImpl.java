package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public BinaryContent create(BinaryContentCreate binaryContentCreate) {
        try {
            // 파일 업로드
            //uploadedFiles 디렉토리에 {UUID.확장자} 형태로 저장
            String uploadDir = "uploadedFiles/";
            String originalFilename = binaryContentCreate.file().getOriginalFilename();
            String fileExtension;   //확장자추출
            if (originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
                fileExtension = "";
            } else fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 경로 지정
            UUID binaryContentId = UUID.randomUUID();
            Path path = Paths.get(uploadDir + binaryContentId + fileExtension);
            File newFile = new File(String.valueOf(path));
            try (FileOutputStream fos = new FileOutputStream(newFile)) {
                fos.write(binaryContentCreate.file().getBytes());
            }
            // entity 생성, 저장
            BinaryContent binaryContent = new BinaryContent(binaryContentId, originalFilename, binaryContentCreate.file().getContentType(),
                    String.valueOf(path));
            binaryContentRepository.save(binaryContent);
            return binaryContent;
        } catch (IOException e) {   // MultipartFile 오류 발생 시
            return null;
        }
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent Not Found"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> uuids) {
        List<BinaryContent> list = new ArrayList<>();
        for (UUID id : uuids) {
            list.add(find(id));
        }
        return list;
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.delete(id);
    }


}
