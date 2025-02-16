package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final ChannelRepository channelRepository;


    //리드스테이터스 객체 생성하여 레포지토리에 저장
    @Override
    public UUID createReadStatus(UUID userId, UUID channelId) {
        if (userId == null || channelId == null) {
            System.out.println("readStatus 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        try {
            if (channelRepository.isChannelExist(channelId)==false||userRepository.isUserExistByUUID(userId)==false) {
                throw new NoSuchElementException("ReadStatus 생성 실패. 해당 userId 혹은 channelId에 해당하는 객체를 찾을 수 없습니다.");
            }
            ReadStatus newReadStatus = new ReadStatus(userId);
            readStatusRepository.saveReadStatus(channelId, userId, newReadStatus);
            return newReadStatus.getUserId();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("readStatus 생성 실패.");
            return null;
        }
    }

    //리드스테이터스 객체 찾아서 반환
    @Override
    public ReadStatusDto findReadStatusByUserId(UUID channelId, UUID userId) {
        if (userId == null || channelId == null) {
            System.out.println("readStatus 리턴 실패. 입력값을 확인해주세요.");
            return null;
        }
        try {
            ReadStatus readStatus = readStatusRepository.getReadStatus(channelId, userId);
            if (readStatus != null) {
                return ReadStatusDto.from(readStatus);
            } else {
                System.out.println("readStatus 리턴 실패.");
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("readStatus 생성 실패.");
            return null;
        }
    }

    //유저가 속한 채널이름을 키로, 유저리드스테이트 객체를 값으로 저장한 해쉬맵 리턴.
    @Override
    public HashMap<UUID, ReadStatusDto> findAllReadStatusByUserId(UUID userId) {
        List<ChannelDto> channelListContainsUser = channelService.findAllByUserId(userId);
        if (channelListContainsUser == null || channelListContainsUser.size() == 0) {
            System.out.println("유저가 속한 모든 readStatus 리스트 반환 실패.");
            return null;
        }
        HashMap<UUID, ReadStatusDto> userReadStatusMap = new HashMap<UUID, ReadStatusDto>();
        channelListContainsUser.forEach(channelDto -> userReadStatusMap.put(channelDto.id(), findReadStatusByUserId(channelDto.id(), userId)));
        return userReadStatusMap;
    }

    //리드스테이터스객체 updatedAt 변경
    @Override
    public boolean updateReadStatus(UUID channelId, UUID userId) {
        if (userId == null || channelId == null) {
            System.out.println("readStatus 업데이트 실패. 입력값을 확인해주세요.");
            return false;
        }
        try {
            ReadStatus readStatus = readStatusRepository.getReadStatus(channelId, userId);
            if (readStatus != null) {
                readStatus.setUpdatedAt();
                readStatusRepository.saveReadStatus(channelId, userId, readStatus);
                System.out.println("readStatus 업데이트 성공.");
                return true;
            } else {
                System.out.println("readStatus 업데이트 실패.");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("readStatus 업데이트 실패.");
            return false;
        }
    }

    //리드스테이터스객체 삭제
    @Override
    public boolean deleteReadStatus(UUID channelId, UUID userId){
        if (userId == null || channelId == null) {
            System.out.println("readStatus 삭제. 입력값을 확인해주세요.");
            return false;
        }
        try {
            if (readStatusRepository.deleteReadStatus(channelId, userId)){
                System.out.println("readStatus 삭제 성공.");
                return true;
            }else{
                System.out.println("readStatus 삭제 실패.");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("readStatus 삭제 실패.");
            return false;
        }
    }
}
