package discodeit.repository.file;

import discodeit.entity.BinaryContent;
import discodeit.repository.BinaryContentRepository;
import discodeit.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@ConditionalOnProperty(value = "repository.type", havingValue = "file")
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private Map<String, BinaryContent> binaryData;
    private final Path path;

    public FileBinaryContentRepository(@Value("${repository.binary-content-file-path}") Path path) {
        this.path = path;
        if (!Files.exists(this.path)) {
            try {
                Files.createFile(this.path);
                FileUtil.save(this.path, new HashMap<>());
            } catch (IOException e) {
                throw new RuntimeException("[error] 바이너리 데이터 파일을 초기화 불가능", e);
            }
        }
        FileUtil.init(this.path);
        this.binaryData = FileUtil.load(this.path, BinaryContent.class);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryData.put(binaryContent.getId().toString(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        if (!existsById(id)) {
            throw new NoSuchElementException("[error] 해당 ID의 바이너리 데이터가 존재하지 않습니다.");
        }
        return Optional.ofNullable(binaryData.get(id.toString()));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryData.values().stream().filter(content -> binaryContentIds.contains(content.getId())).toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return binaryData.containsKey(id.toString());
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new NoSuchElementException("[error] 해당 ID의 바이너리 데이터가 존재하지 않습니다.");
        }
        binaryData.remove(id.toString());
    }

}
