package com.sprint.mission.discodeit.service.domain;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.repository.domain.BinaryContentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BinaryContentService implements BinaryContentRepository {
    //이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        return null;
    }

    @Override
    public BinaryContent findById(UUID id) {
        return null;
    }

    @Override
    public List<BinaryContent> findByDomainId(UUID domainId) {
        return List.of();
    }

    @Override
    public List<BinaryContent> findAll() {
        return List.of();
    }

    @Override
    public void update(UUID id, BinaryContent binaryContent) {

    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public void deleteByDomainId(UUID domainId) {

    }
}
