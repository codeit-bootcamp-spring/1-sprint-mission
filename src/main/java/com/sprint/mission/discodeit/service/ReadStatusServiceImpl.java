package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {
    @Autowired
    private final ReadStatusRepository readStatusRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(CreateReadStatusRequest request) {
        if (!channelRepository.existsById(request.getChannelid())){
            throw new NoSuchElementException("channel not found");
        }
        if(!userRepository.existsById(request.getUserid())){
            throw new NoSuchElementException("user not found");
        }
        if(readStatusRepository.isUserMemberOfChannel(request.getChannelid(), request.getUserid())){
            throw new NoSuchElementException("user already has member of channel");
        }

        ReadStatus readStatus=new ReadStatus(request.getUserid(),request.getChannelid());
        readStatusRepository.saveReadStatus(List.of(readStatus));
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> find (UUID id) {
        return Optional.ofNullable(readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("readStatus not found")));
    }

    @Override
    public List<ReadStatus> findAllByUserid(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(UpdateReadStatusRequest request) {
        ReadStatus readStatus=readStatusRepository.findById(request.getId())
                .orElseThrow(()->new NoSuchElementException("ReadStatus id not found"));
        ReadStatus updateReadStatus=new ReadStatus(request.getId(),request.getChannelId());
        readStatusRepository.saveReadStatus(List.of(readStatus));
        return updateReadStatus;
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.deleteById(id);
    }

    @Override
    public void deleteByChannelId(UUID id) {
        readStatusRepository.deleteByChannelId(id);
    }


    @Override
    public void addMembersToChannel(UUID channelId, List<UUID> memberIds) {
        List<ReadStatus> readStatuses = memberIds.stream()
                .map(memberId -> new ReadStatus(memberId, channelId))
                .collect(Collectors.toList());
        readStatusRepository.saveReadStatus(readStatuses);
    }
}
