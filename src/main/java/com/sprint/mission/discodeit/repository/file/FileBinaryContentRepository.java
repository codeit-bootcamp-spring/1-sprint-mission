package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String BinaryContentRepositoryPath = System.getProperty("user.dir") + "\\serFiles\\BinaryContent\\";



    @Override
    public BinaryContent getBinaryContent(BinaryContentType type, UUID binaryContentId) {
        return fileIOHandler.deserializeBinaryContent(BinaryContentRepositoryPath+binaryContentId.toString());
    }

    @Override
    public boolean deleteBinaryContent(UUID binaryContentId) {
        return fileIOHandler.deleteFile(BinaryContentRepositoryPath+binaryContentId.toString());
    }

    @Override
    public boolean saveBinaryContent(BinaryContent binaryContent) {
        return fileIOHandler.serializeBinaryContent(binaryContent, System.getProperty("user.dir") + "\\serFiles\\BinaryContent\\"+binaryContent.getId().toString());
    }

    @Override
    public boolean isBinaryContentExist(BinaryContentType type, UUID binaryContentId) {
        return fileIOHandler.deserializeBinaryContent(BinaryContentRepositoryPath+binaryContentId.toString()) != null;
    }
}