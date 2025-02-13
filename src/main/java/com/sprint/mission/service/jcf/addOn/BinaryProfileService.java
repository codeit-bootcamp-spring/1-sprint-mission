package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.entity.addOn.BinaryProfileContent;
import com.sprint.mission.repository.jcf.addOn.BinaryProfileRepository;
import com.sprint.mission.dto.request.BinaryProfileContentDto;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BinaryProfileService {
    //create
    //[ ] DTO를 활용해 파라미터를 그룹화합니다.
    //find
    //[ ] id로 조회합니다.
    //findAllByIdIn
    //[ ] id 목록으로 조회합니다.
    //delete
    //[ ] id로 삭제합니다.

    private final BinaryProfileRepository repository;

    public void create(BinaryProfileContentDto dto) {
        repository.save(new BinaryProfileContent(dto));
    }

    public BinaryProfileContentDto findById(UUID userId) {
        return repository.findById(userId)
                .map(profileContent
                        -> new BinaryProfileContentDto(profileContent))
                .orElseThrow(() -> new NotFoundId("Fail to find : wrong id"));
    }

    public List<BinaryProfileContentDto> findAll() {
        return repository.findAll().stream()
                .map(bpc -> new BinaryProfileContentDto(bpc))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void delete(UUID messageId) {
        repository.delete(messageId);
    }


}
