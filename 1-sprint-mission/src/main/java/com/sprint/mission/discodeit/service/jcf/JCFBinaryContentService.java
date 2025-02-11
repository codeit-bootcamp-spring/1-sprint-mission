package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("JCFBinaryContentService")
public class JCFBinaryContentService implements BinaryContentService {
    protected Map<UUID, BinaryContent> binaryContentRepository = new HashMap<>();

    @Override
    public BinaryContent create(char[] img) {
        BinaryContent binaryContent=BinaryContent.setUpBinaryContent(img);
        binaryContentRepository.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }



    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentRepository.get(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.values().stream()
                .filter(bc -> ids.contains(bc.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.remove(id);
    }

}
