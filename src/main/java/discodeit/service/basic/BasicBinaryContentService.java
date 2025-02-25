package discodeit.service.basic;

import discodeit.dto.binarycontent.CreateBinaryContentRequest;
import discodeit.entity.BinaryContent;
import discodeit.entity.BinaryContentType;
import discodeit.repository.BinaryContentRepository;
import discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepo;

    @Override
    public BinaryContent create(CreateBinaryContentRequest request) {
        String filename = request.filename();
        BinaryContentType type = request.type();
        byte[] bytes = request.bytes();
        BinaryContent binaryContent = new BinaryContent(filename, type, (long) bytes.length, bytes);

        return binaryContentRepo.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        return binaryContentRepo.findById(binaryContentId).orElseThrow(() -> new NoSuchElementException("[error] 해당하는 바이너리 컨텐츠 ID가 존재하지 않습니다."));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepo.findAllByIdIn(binaryContentIds).stream().toList();
    }

    @Override
    public void delete(UUID binaryContentId) {
        if (!binaryContentRepo.existsById(binaryContentId)) {
            throw new NoSuchElementException("[error] 해당하는 바이너리 컨텐츠 ID가 존재하지 않습니다.");
        }
        binaryContentRepo.deleteById(binaryContentId);
    }
}
