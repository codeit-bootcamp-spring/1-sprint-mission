package com.spirnt.mission.discodeit.util;

import com.spirnt.mission.discodeit.entity.BinaryContentUploadStatus;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
 * 문제: BasicBinaryContentService의 create() 메서드는 @Transactional(REQUIRES_NEW)로 트랜잭션이 시작된 상태에서 실행됨
 * but 내부에서 호출되는 put() 메서드는 @Async 비동기 방식으로 동작
 * -> 그 안의 thenAccept() / exceptionally()에서 uploadStatus 속성을 업데이트해야는데
 * 이 둘은 원래 트랜잭션 범위 밖에서 실행됨. -> 트랜잭션이 적용되지 않아 db 반영 x
 *
 * 해결: uploadStatus 변경 로직을 @Transactional이 적용된 별도의 빈으로 분리
 * 비동기 콜백 내부에서 이 빈을 호출하여 별도 트랜잭션으로 업데이트 실행
 *
 * + 별도의 빈으로 분리하는 이유:
 * @Transactional은 프록시 기반 AOP로 동작
 * -> 트랜잭션을 적용하려면 메서드를 프록시 객체를 통해 호출해야 함
 * 그런데 자기 자신(클래스) 내부에서 다른 메서드를 호출하면 프록시를 거치지 않으므로 @Transactional이 무시됨
 */
@Component
@RequiredArgsConstructor
public class BinaryContentStatusUpater {

    private final BinaryContentRepository binaryContentRepository;

    @Transactional
    public void uploadStatus(UUID binaryContentId, BinaryContentUploadStatus status) {
        binaryContentRepository.updateUploadStatus(binaryContentId, status);
    }
}
