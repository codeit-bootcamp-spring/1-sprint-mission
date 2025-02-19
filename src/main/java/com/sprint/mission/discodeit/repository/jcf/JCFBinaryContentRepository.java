package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

@ConditionalOnProperty(name = "app.binaryContent-repository", havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {

    HashMap<UUID, LinkedHashMap<UUID, BinaryContent>> allAttachedContentsMap = new HashMap<UUID, LinkedHashMap<UUID, BinaryContent>>();
    LinkedHashMap<UUID, BinaryContent> allProfilePicturesMap = new LinkedHashMap<UUID, BinaryContent>();

    @Override
    public LinkedHashMap<UUID, BinaryContent> getBinaryContentsMap(BinaryContentType type, UUID channelId) {
        return type.equals(BinaryContentType.Aettached_File) ? allAttachedContentsMap.get(channelId) : allProfilePicturesMap;
    }

    @Override
    public BinaryContent getBinaryContent(BinaryContentType type, UUID channelId, UUID binaryContentId) {
        return getBinaryContentsMap(type, channelId).get(binaryContentId);
    }

    @Override
    public boolean deleteBinaryContentMap(BinaryContentType type, UUID channelId) {
        return allAttachedContentsMap.remove(channelId) != null;
    }

    @Override
    public boolean deleteBinaryContent(BinaryContentType type, UUID channelId, UUID binaryContentId) {
        return getBinaryContentsMap(type, channelId).remove(channelId) != null;
    }

    @Override
    public boolean addBinaryContent(BinaryContentType type, UUID channelId, BinaryContent binaryContent) {
        return getBinaryContentsMap(type, channelId).put(binaryContent.getId(), binaryContent) != null;
    }


    @Override
    public boolean saveBinaryContentsMap(BinaryContentType type, UUID channelId, LinkedHashMap<UUID, BinaryContent> binaryContentMap) {
        return allAttachedContentsMap.put(channelId, binaryContentMap) != null;
    }

    @Override
    public boolean isBinaryContentExist(BinaryContentType type, UUID channelId, UUID binaryContentId) {
        return getBinaryContentsMap(type, channelId).get(binaryContentId) != null;
    }

}