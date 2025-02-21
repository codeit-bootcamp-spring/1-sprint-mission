package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("Jcf")
@RequiredArgsConstructor
public class BinaryContentJcfRepositoryImpl implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> binaryContentMap;

    public BinaryContentJcfRepositoryImpl() {
        this.binaryContentMap =  new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContentDto binaryContentDto) {
        BinaryContent binaryContent = new BinaryContent(binaryContentDto.domainId(), binaryContentDto.multipartFile());
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
    public void update(UUID id, BinaryContentDto binaryContentDto) {
        //프로필 업데이트에 사용됨
        BinaryContent existBinaryContent = binaryContentMap.get(id);
        if (existBinaryContent.getMultipartFile() != null) {
            existBinaryContent = existBinaryContent.update(binaryContentDto.multipartFile());
        }
        binaryContentMap.replace(id, existBinaryContent);
    }
}
