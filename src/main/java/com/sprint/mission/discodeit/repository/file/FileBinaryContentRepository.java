package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path directory;
    private final String extension;

    public FileBinaryContentRepository(RepositoryProperties properties) {
        this.directory = Paths.get(System.getProperty(properties.getBaseDirectory()))
                .resolve(properties.getFileDirectory())
                .resolve("binary");
        this.extension = properties.getExtension();
        FileService.init(this.directory);
    }

    @Override
    public BinaryContent save(CreateBinaryContentDto createBinaryContentDto) {
        BinaryContent binaryContent = new BinaryContent(createBinaryContentDto.binaryImage(), createBinaryContentDto.createdAt());
        Path binarayContentPath = directory.resolve(binaryContent.getId().concat(extension));
        FileService.save(binarayContentPath, binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(String id) {
        Path binaryContentPath = directory.resolve(id.concat(extension));
        return (BinaryContent) FileService.read(binaryContentPath);
    }

    @Override
    public List<BinaryContent> findAll() {
        return FileService.load(directory);
    }

    @Override
    public boolean delete(String id) {
        return FileService.delete(directory.resolve(id.concat(extension)));
    }
}
