package com.sprint.mission.discodeit.study;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.error.execption.bianryContent.BinaryContentNofFoundException;
import com.sprint.mission.discodeit.global.util.MultipartFileConverter;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TempBinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final MultipartFileConverter multipartFileConverter;

    public BinaryContent createBinaryContent(MultipartFile multipartFile) {
        byte[] converterByteArray = multipartFileConverter.toByteArray(multipartFile);
        BinaryContent binaryContent = BinaryContent.of(converterByteArray);

        return binaryContentRepository.saveBinaryContent(binaryContent);
    }

    public BinaryContent createBinaryContent(BinaryContent binaryContent) {
        return binaryContentRepository.saveBinaryContent(binaryContent);
    }

    public BinaryContent findBinaryContentById(UUID binaryContentId) {
        return Optional.ofNullable(binaryContentRepository.findBinaryContentById(binaryContentId))
                .orElseThrow(() -> new BinaryContentNofFoundException("id: " + binaryContentId));
    }

    public List<BinaryContent> findAllBinaryContentsById(List<UUID> binaryContentIdList) {
        return binaryContentIdList.stream()
                .map(binaryContentId -> findBinaryContentById(binaryContentId))
                .toList();
    }

    public void deleteBinaryContent(UUID binaryContentId) {
        findBinaryContentById(binaryContentId);

        binaryContentRepository.removeBinaryContent(binaryContentId);
    }

    public MultipartFile getMultipartFile(BinaryContent binaryContent) {
        return multipartFileConverter.toMultipartFile(binaryContent.getContent());
    }

}
