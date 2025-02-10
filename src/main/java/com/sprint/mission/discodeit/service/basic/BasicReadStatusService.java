package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelServiceFindDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceFindDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserService userService;
    private final ChannelService channelService;


    @Override
    public UUID create(ReadStatusCreateDTO dto) {
        UserServiceFindDTO userServiceFindDTO = userService.find(dto.getUserId());
        ChannelServiceFindDTO channelServiceFindDTO = channelService.find(dto.getChannelId());
        checkDuplicateReadStatus(userServiceFindDTO.getId(), channelServiceFindDTO.getId());

        ReadStatus readStatus = new ReadStatus(userServiceFindDTO.getId(), channelServiceFindDTO.getId());
        return readStatus.getId();
    }

    @Override
    public ReadStatus find(UUID id) {
        ReadStatus findReadStatus = readStatusRepository.findOne(id);
        Optional.ofNullable(findReadStatus)
                .orElseThrow(() -> new NoSuchElementException("해당하는 findReadStatus 가 없습니다."));
        return findReadStatus;
    }

    @Override
    public List<ReadStatus> findAll(){
        return readStatusRepository.findAll();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateDTO readStatusUpdateDTO) {
        ReadStatus findReadStatus = readStatusRepository.findOne(readStatusUpdateDTO.getId());
        findReadStatus.updateReadStatus(readStatusUpdateDTO.getTime());
        readStatusRepository.update(findReadStatus);
        return findReadStatus;
    }

    @Override
    public UUID delete(UUID id) {
        return readStatusRepository.delete(id);
    }

    private void checkDuplicateReadStatus(UUID userId, UUID channelId) {
        if (readStatusRepository.findByUserIdAndChannlId(userId, channelId).isPresent()) {
            throw new IllegalArgumentException("중복된 ReadStatus 가 존재합니다. userid: " + userId+" channelId :"+channelId);
        }

        //방법1 findByUserId 정의해서 넘어온 값이 null이면 중복 x
        //List로 넘어오는 경우이다.
/*        List<UserStatus> findUserStatus = userStatusRepository.findByUserId(userId);

        if (!findUserStatus.isEmpty()) {
            throw new IllegalArgumentException("중복된 UserStatus 가 존재합니다. Userid: " + userId);
        }*/


        //중복 검사 Optional1
/*        Optional.ofNullable(userStatusRepository.findByUserId(userId))
                .filter(list -> !list.isEmpty()) // 리스트가 비어있지 않으면 예외 발생
                .ifPresent(list -> {
                    throw new DuplicateUserStatusException("UserStatus already exists for userId: " + userId);
                });*/

    }
}
