package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor // TODO : 이게 의존성 자동 주입?
@Service
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    @Override
    // 사용자 A가 10시에 채널에 방문 -> 10시 이전에 온 메세지들은 다 읽은 걸로 간주 -> 10시 이후에 온 메세지들은 안읽음 처리
    // 일단 회원, 채널이 존재하지 않으면 예외 처리, 객체가 이미 존재하면 예외처리
    // TODO 질문 : ReadStatus 엔티티에도 있을 거면 따로 DTO로 만드는 이유가 뭘까?, 단일 DTO일 거면 따로 DTO로 만드는 이유가 뭘끼?
    // TODO 질문 : 그리고 파라미터로 DTO 전달하고 바로 메서드 안에서 일반 변수에 할당해서 쓸거면 왜 DTO를 쓰는 것인가?
    public ReadStatus create(ReadStatusCreateRequest request){
        // TODO 질문이랑 직결되는 첫 두줄
        UUID userId = request.userId();
        UUID channelId = request.channelId();
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("아이디가" + userId + "인 회원이 존재하지 않습니다.");
        }
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("아이디가" + channelId + "인 채널이 존재하지 않습니다.");
        }
        List<ReadStatus> readList = readStatusRepository.findAllByUserId(request.userId());
        for (ReadStatus status : readList){
            if (status.getUserId().equals(request.userId()) && status.getChannelId().equals(request.channelId())){
                throw new IllegalArgumentException("해당 Read Status가 이미 존재합니다.");
            }
        }// 읽음상태 리스트에서 해당 회원과 채널 아이디와 동일한 객체가 있으면 예외
         // readStatusRepository.data -> forEach -> 그 객체의 회원, 채널 아이디 둘 다 일치

        // TODO 질문이랑 직결되는 부분
        Instant lastReadAt = request.lastReadAt();
        ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
        return readStatusRepository.save(readStatus);

    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("해당 Read Status가 존재하지 않습니다."));
        return readStatus;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        // 파라미터인 회원이 존재하는지 봐야지
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("아이디가" + userId + "인 회원이 존재하지 않습니다.");
        }
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public void update(UUID readStatusId, ReadStatusUpdateRequest request){
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("해당 Read Status가 존재하지 않습니다."));
        Instant newLastReadAt = request.newLastReadAt();
        readStatus.update(newLastReadAt);
    }

    @Override
    public void delete(UUID readStatusId) {
        // readStatus가 존재하는지 예외 처리
        if (!userRepository.existsById(readStatusId)) {
            throw new NoSuchElementException("해당 Read Status가 존재하지 않습니다.");
        }
        readStatusRepository.deleteById(readStatusId);
    }
}
