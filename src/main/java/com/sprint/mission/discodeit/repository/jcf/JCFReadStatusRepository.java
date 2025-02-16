package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFReadStatusRepository implements ReadStatusRepository {

    // 외부에서 생성자 접근 불가
    private JCFReadStatusRepository() throws Exception {
    }
    //해쉬맵 포함 관계:  allReadStatusMaps(모든 채널별 맵 저장) > channelReadStatusMap(채널별) > readStatus(채널에 속한 멤버별)


    //모든 채널의 channelReadStatusMap들을 통합 관리하는 allReadStatusMaps 반환
    //해쉬맵이 해쉬맵을 갖고있는 것이 좋지않은 구조인것은 알지만, 채널별로 나뉜 폴더에서 직렬화해서 사용하는 file레포지토리와 달리
    //JCF~는 클래스 내부에 모두 저장해야하기때문에 불가피하게 해쉬맵을 값으로 가진 해쉬맵을 생성하여 관리하게 되었음.
    HashMap<UUID, HashMap> allReadStatusMaps = new HashMap<UUID, HashMap>();

    //채널별 맵인 channelReadStatusMap을 반환
    @Override
    public HashMap<UUID, ReadStatus> getChannelReadStatusMap(UUID channelMapId) {
        return allReadStatusMaps.get(channelMapId);
    }

    @Override
    public ReadStatus getReadStatus(UUID channelId, UUID userId) throws IOException {
        HashMap<UUID, ReadStatus> channelMap = getChannelReadStatusMap(channelId);
        return channelMap.get(userId);
    }

    //채널별 맵인 channelReadStatusMap을 allReadStatusMaps에 저장
    @Override
    public boolean addChannelReadStatusMap(UUID channelId, HashMap<UUID, ReadStatus> readStatusMap) throws IOException {
        this.allReadStatusMaps.put(channelId, readStatusMap);
        return true;
    }

    //readStatus 객체를 채널별 맵 channelReadStatusMap에 저장
    @Override
    public boolean saveReadStatus(UUID channelId, UUID userId, ReadStatus readStatus) throws IOException {
        HashMap<UUID, ReadStatus> readStatusMap = Optional.ofNullable(this.allReadStatusMaps.get(channelId)).orElseThrow(() -> new NoSuchElementException("해당 채널의 readStatusMap이 레포지토리에 존재하지 않습니다."));
        readStatusMap.put(userId, readStatus);
        return true;
    }

    //채널 readStatusMap의 readStatus를 찾아 삭제함.
    //정상적으로 삭제될경우 삭제된 요소가 반환되므로 null이 반환될경우 존재하지 않는 key를 삭제 시도한 것으로 보고 예외를 던짐.
    @Override
    public boolean deleteReadStatus(UUID channelId, UUID userId) throws IOException {
        HashMap<UUID, ReadStatus> readStatusMap = Optional.ofNullable(this.allReadStatusMaps.get(channelId)).orElseThrow(() -> new NoSuchElementException("해당 채널의 readStatusMap이 레포지토리에 존재하지 않습니다."));
        if (readStatusMap.remove(userId).equals(null)) {
            throw new NoSuchElementException("해당 uuid를 가진 readStatus 객체가 채널 readStatusMap에 없습니다.");
        }
        return true;
    }

    @Override
    public boolean deleteChannelReadStatusMap(UUID channelId) {
        if (channelId == null) {
            return false;
        }
        if (allReadStatusMaps.remove(channelId)==null){
            return false;
        }else{
            return true;
        }
    }


}

