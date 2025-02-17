package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String BinaryContentRepositoryPath = System.getProperty("user.dir") + "\\serFiles\\BinaryContent\\";


    @Override
    public LinkedHashMap<UUID, BinaryContent> getBinaryContentsMap(BinaryContentType type, UUID channelId) {
        return (LinkedHashMap<UUID, BinaryContent>) fileIOHandler.deserializeLinkedHashMap("BinaryContent\\"+type.name()+channelId.toString());
    }

    @Override
    public BinaryContent getBinaryContent(BinaryContentType type, UUID channelId, UUID binaryContentId) {
        return getBinaryContentsMap(type, channelId).get(binaryContentId);
    }

    @Override
    public boolean deleteBinaryContentMap(BinaryContentType type, UUID channelId) {
        return fileIOHandler.deleteFile("BinaryContent\\"+type.name()+channelId.toString());
    }

    @Override
    public boolean deleteBinaryContent(BinaryContentType type, UUID channelId, UUID binaryContentId) {
        LinkedHashMap<UUID, BinaryContent> binaryContentMap = getBinaryContentsMap(type, channelId);
        binaryContentMap.remove(binaryContentId);
        return saveBinaryContentsMap(type, channelId, binaryContentMap);
    }

    @Override
    public boolean addBinaryContent(BinaryContentType type, UUID channelId, BinaryContent binaryContent) {
        LinkedHashMap<UUID, BinaryContent> binaryContentMap = getBinaryContentsMap(type, channelId);
        binaryContentMap.put(binaryContent.getId(), binaryContent);
        return saveBinaryContentsMap(type, channelId, binaryContentMap);
    }


    @Override
    public boolean saveBinaryContentsMap(BinaryContentType type, UUID channelId, LinkedHashMap<UUID, BinaryContent> binaryContentMap) {
        return fileIOHandler.serializeLinkedHashMap(binaryContentMap, "BinaryContent\\"+type.name()+channelId.toString());
    }

    @Override
    public boolean isBinaryContentExist(BinaryContentType type, UUID channelId, UUID binaryContentId) {
        LinkedHashMap<UUID, BinaryContent> binaryContentMap = getBinaryContentsMap(type, channelId);
        return binaryContentMap.containsKey(binaryContentId);
    }


}