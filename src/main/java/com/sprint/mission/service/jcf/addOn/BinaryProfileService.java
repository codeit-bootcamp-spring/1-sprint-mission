package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.common.CustomException;
import com.sprint.mission.common.ErrorCode;
import com.sprint.mission.entity.addOn.BinaryProfileContent;
import com.sprint.mission.repository.jcf.addOn.BinaryProfileRepository;
import com.sprint.mission.dto.request.BinaryProfileContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BinaryProfileService {

    private final BinaryProfileRepository profileRepository;

    public void create(BinaryProfileContentDto dto) {
        profileRepository.save(new BinaryProfileContent(dto));
    }

    public BinaryProfileContent findById(UUID userId) {
        return profileRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_PROFILE_MATCHING_USER));
    }

    public List<BinaryProfileContent> findAll() {
        return profileRepository.findAll();
//                .map(bpc -> new BinaryProfileContentDto(bpc))
//                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void delete(UUID userId) {
        if (profileRepository.isExistById(userId)) throw new CustomException(ErrorCode.NO_SUCH_PROFILE_MATCHING_USER);
        else profileRepository.delete(userId);
    }
}
