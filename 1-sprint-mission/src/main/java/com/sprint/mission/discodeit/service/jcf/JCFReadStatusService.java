package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.DTO.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exepction.DuplicateReadStatusException;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("JCFReadStatusService")
public class JCFReadStatusService implements ReadStatusService {

    protected Map<UUID, ReadStatus> readStatusMap;



    public JCFReadStatusService() {
        this.readStatusMap= new HashMap<>();
    }

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {

        boolean duplicateExists = readStatusMap.values().stream()
                .anyMatch(rs -> rs.getChannelId().equals(request.channelId())
                        && rs.getUserId().equals(request.userId()));
        if (duplicateExists) {
            throw new DuplicateReadStatusException("","");
        }

        ReadStatus newReadStatus = ReadStatus.setUpReadStatus(request.channelId(), request.userId(), request.isRead());
        readStatusMap.put(newReadStatus.getId(), newReadStatus);
        return newReadStatus;
    }

    @Override
    public ReadStatus findById(UUID id) {
        return readStatusMap.get(id);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(readStatus ->
                        readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public boolean update(ReadStatusUpdateRequest request) {
        ReadStatus target=readStatusMap.get(request.id());
        if(target.isRead()!=request.isRead()){
            target.updateIsRead();
            return true;
        }else
            return false;
    }

    @Override
    public void delete(List<ReadStatus> list) {
        for(ReadStatus i:list){
            readStatusMap.remove(i.getId());
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        readStatusMap.values().removeIf(readStatus ->
                readStatus.getUserId().equals(userId));
    }
    @Override
    public void addReadStatus(ReadStatus readStatus){
        readStatusMap.put(readStatus.getId(),readStatus);
    }
}
