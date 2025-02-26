package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.config.FileRepositoryCondition;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Repository
@FileRepositoryCondition
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final FileStorage fileStorage;
    private final Path directory;
    private static final String SUBDIRECTORY = "binaryContent";

    public FileBinaryContentRepository(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        directory = fileStorage.getDirectory(SUBDIRECTORY);
        fileStorage.init(directory);
    }

    @Override
    public BinaryContent saveBinaryContent(BinaryContent binaryContent) {
        Path filePath = fileStorage.getFilePath(directory, binaryContent.getId());
        return fileStorage.save(filePath, binaryContent);
    }

    @Override
    public BinaryContent findBinaryContentById(UUID binaryContentId) {
        List<BinaryContent> binaryContentList = fileStorage.load(directory);

        return binaryContentList.stream()
                .filter(binaryContent -> binaryContent.getId().equals(binaryContentId))
                .findFirst().orElse(null);
    }

    @Override
    public List<BinaryContent> findAllBinaryContentById(List<UUID> binaryContentIdList) {
        List<BinaryContent> binaryContentList = fileStorage.load(directory);

        return binaryContentIdList.stream()
                .map(binaryContentId ->
                        binaryContentList.stream().filter(
                                binaryContent -> binaryContent.getId().equals(binaryContentId)
                        ).findFirst().orElse(null))
                .toList();
    }

    @Override
    public void removeBinaryContent(UUID binaryContentId) {
        BinaryContent binaryContent = findBinaryContentById(binaryContentId);
        fileStorage.remove(directory, binaryContent);
    }
}
