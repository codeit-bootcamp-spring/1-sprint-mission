package com.sprint.mission.discodeit.repository.domain.jcf;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Profile("Jcf")
public class BinaryContentJcfRepositoryImpl implements BinaryContentRepository {
    private Map<UUID, BinaryContent> binaryContentMap;

    public BinaryContentJcfRepositoryImpl(Map<UUID, BinaryContent> binaryContentMap) {
        this.binaryContentMap = binaryContentMap;
    }

    @Override
    public BinaryContent save(BinaryContentDto binaryContentDto) {
        BinaryContent binaryContent = new BinaryContent(binaryContentDto.domainId(), binaryContentDto.file());
        binaryContentMap.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentMap.get(id);
    }

    @Override
    public List<BinaryContent> findByDomainId(UUID domainId) {
        return binaryContentMap.values().stream().filter(s -> s.getDomainId().equals(domainId)).collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(binaryContentMap.values());
    }

    @Override
    public void delete(UUID id) {
        binaryContentMap.remove(id);
    }

    @Override
    public void deleteByDomainId(UUID domainId) {
        List<BinaryContent> list = binaryContentMap.values().stream().filter(s -> s.getDomainId().equals(domainId)).toList();
        for (BinaryContent binaryContent : list) {
            binaryContentMap.remove(binaryContent.getId());
        }
    }

    @Override
    public void update(UUID id, BinaryContent binaryContent) {
        //프로필 업데이트에 사용됨
        BinaryContent existBinaryContent = binaryContentMap.get(id);
        if (existBinaryContent.getFile() != null) {
            existBinaryContent = existBinaryContent.update(binaryContent.getFile());
        }
        binaryContentMap.replace(id, existBinaryContent);
    }
}
