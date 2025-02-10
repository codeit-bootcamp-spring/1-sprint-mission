package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusFindDTO;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final ChannelService channelService;
    private final UserService userService;

    @Override
    public ReadStatus create(ReadStatusCreateDTO readStatusCreateDTO) {
        try {
            //채널존재확인
            channelService.readChannel(readStatusCreateDTO.channelId());
            userService.findUserDTO(readStatusCreateDTO.userId());
        } catch (Exception exception) {
            throw new IllegalArgumentException("ReadStatus를 생성할 수 없습니다.");
        }

        ReadStatus readStatus = new ReadStatus(readStatusCreateDTO.channelId(), readStatusCreateDTO.userId());

        // 이미 채널id와 userId가 동일한 readStatus 존재시 예외
        if (readStatusRepository.load().values().stream()
                .anyMatch(readStatus1 -> readStatus1.getChannelId().equals(readStatus.getChannelId())
                && readStatus1.getUserId().equals(readStatus.getUserId())))
        {
            throw new IllegalArgumentException("이미 존재하는 ReadStatus입니다.");
        }

        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus findbyId(UUID uuid) {
        return readStatusRepository.findById(uuid);//repo구현필요
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);//repo구현필요
    }

    @Override
    public ReadStatus update(ReadStatusUpdateDTO readStatusUpdateDTO) {
        ReadStatus readStatus = findbyId(readStatusUpdateDTO.id());
        readStatus.update();
        readStatusRepository.save(readStatus);
        return null;
    }

    @Override
    public void delete(UUID uuid) {
        readStatusRepository.delete(uuid);
    }
}
