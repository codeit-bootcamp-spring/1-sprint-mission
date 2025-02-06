package com.sprint.mission.discodeit.service.domain;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService{
    //사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
    private final com.sprint.mission.discodeit.repository.ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public void save(ReadStatusDto readStatusDto) {
        if (readStatusDto.channelId() == null || readStatusDto.userId() == null) {
            throw new IllegalStateException("채널 아이디, 유저 아이디가 없습니다.");
        }
        try {
            User user = userRepository.findUserById(readStatusDto.userId());
            Channel channel = channelRepository.findById(readStatusDto.channelId());
            readStatusRepository.save(new ReadStatusDto(null, user.getId(), channel.getId(), null, null, null));
        }catch (Exception e) {
            //맞는 예외 반환 값 알아보기
            throw new IllegalStateException("채널 아이디, 유저 아이디가 없습니다.");
        }
    }

    public ReadStatus findById(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id);
        if (readStatus == null) {
            System.out.println("아이디를 찾을 수 없습니다.");
            return null;
        }
        return readStatus;
    }

    public List<ReadStatus> findAll() {
        return readStatusRepository.findAll();
    }

    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            System.out.println("채널을 찾을 수 없습니다.");
            return null;
        }
        return readStatusRepository.findAllByChannelId(channelId);
    }

    public void delete(UUID id) {
        if(readStatusRepository.findById(id)!=null) {
            readStatusRepository.delete(id);
            System.out.println("상태 정보 삭제 성공");
        }else System.out.println("아이디를 찾을 수 없습니다.");
    }

    public void deleteByUserId(UUID userId) {
        if(readStatusRepository.findAllByUserId(userId).stream().findAny().isPresent()) {
            readStatusRepository.deleteByUserId(userId);
            System.out.println("삭제 성공");
        }else System.out.println("유저 아이디를 찾을 수 없습니다.");
        readStatusRepository.deleteByUserId(userId);
    }

    public void deleteByChannelId(UUID channelId) {
        if(readStatusRepository.findAllByChannelId(channelId).stream().findAny().isPresent()) {
            readStatusRepository.deleteByChannelId(channelId);
            System.out.println("삭제 성공");
        }else System.out.println("채널 아이디를 찾을 수 없습니다.");
        readStatusRepository.deleteByChannelId(channelId);
    }

    public void update(ReadStatusDto readStatusDto) {
        if(readStatusRepository.findById(readStatusDto.id())!=null) {
            readStatusRepository.update(readStatusDto);
            System.out.println("업데이트 성공");
        }else System.out.println("상태 정보를 찾을 수 없습니다.");
    }
}
