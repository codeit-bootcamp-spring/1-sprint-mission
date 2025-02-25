package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path binaryContentDirectory;
    private final String extension = ".ser";

    public FileBinaryContentRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.binaryContentDirectory = Paths.get(fileDirectory).resolve("binary");
        FileService.init(this.binaryContentDirectory);
    }

    @Override
    public BinaryContent save(CreateBinaryContentDto createBinaryContentDto) {
        BinaryContent binaryContent = new BinaryContent(createBinaryContentDto.binaryImage(), createBinaryContentDto.createdAt());
        Path binarayContentPath = binaryContentDirectory.resolve(binaryContent.getId().concat(extension));
        FileService.save(binarayContentPath, binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(String id) {
        Path binaryContentPath = binaryContentDirectory.resolve(id.concat(extension));
        List<BinaryContent> load = FileService.load(binaryContentPath);
        if (load.isEmpty()) {
            return null;
        }
        return load.get(0);
    }

    @Override
    public List<BinaryContent> findAll() {
        return FileService.load(binaryContentDirectory);
    }

    @Override
    public boolean delete(String id) {
        return FileService.delete(binaryContentDirectory.resolve(id.concat(extension)));
    }
}
