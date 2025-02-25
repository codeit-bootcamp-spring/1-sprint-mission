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

@RequiredArgsConstructor // TODO : 빈 등록 & 의존성 주입을 위한 필수 -> 생성자. => @RequiredArgsConstructor
@Service // 빈 등록
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository; // final로 해야 롬복이 인식


    @Override
    // 사용자 A가 10시에 채널에 방문 -> 10시 이전에 온 메세지들은 다 읽은 걸로 간주 -> 10시 이후에 온 메세지들은 안읽음 처리
    // 일단 회원, 채널이 존재하지 않으면 예외 처리, 객체가 이미 존재하면 예외처리
    // TODO 질문 : ReadStatus 엔티티에도 있을 거면 따로 DTO로 만드는 이유가 뭘까? - 필요한 정보만 전달할 수 있으니까 (성능최적화, 보안)
    // TODO 질문 : 단일 DTO일 거면 따로 DTO로 만드는 이유가 뭘끼? - 추후 필드가 추가 될 수도 있는 확장성(유연성) 고려, 해당 파라미터 전달이 해당 request를 위함이다 라는 목적을 명시하는 용도
    // TODO : +@ 엔티티를 통째로 전달하는 것의 치명적인 단점 : DB(엔티티, 도메인 모델)을 수정하는 게 API에도 불필요하게 영향을 미칠 수 있음(유지보수성). 그래서 DTO 쓴다~
    public ReadStatus create(ReadStatusCreateRequest request){
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
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request){
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("해당 Read Status가 존재하지 않습니다."));
        Instant newLastReadAt = request.newLastReadAt();
        readStatus.update(newLastReadAt);
        return readStatusRepository.save(readStatus);
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
