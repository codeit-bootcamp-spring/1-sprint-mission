package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import com.sprint.mission.entity.addOn.BinaryProfileContent;
import com.sprint.mission.repository.jcf.addOn.BinaryProfileRepository;
import com.sprint.mission.dto.request.BinaryProfileContentDto;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BinaryProfileService {

    private final BinaryProfileRepository repository;

    public void create(BinaryProfileContentDto dto) {
        repository.save(new BinaryProfileContent(dto));
    }

    public BinaryProfileContent findById(UUID userId) {
        return repository.findById(userId).orElse(new BinaryProfileContent());
//                .map(profileContent
//                        -> new BinaryProfileContentDto(profileContent))
//                .orElseThrow(NotFoundId::new);
    }

    public List<BinaryProfileContent> findAll() {
        return repository.findAll();
//                .map(bpc -> new BinaryProfileContentDto(bpc))
//                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void delete(UUID messageId) {
        if (repository.isExistById(messageId)) throw new NotFoundId();
        else repository.delete(messageId);
    }
}
