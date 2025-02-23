package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// channel이 private과 public으로 나뉘면서 read() 메서드에서 반환하는 것에 문제 생김
// -> public 채널과 private 채널 dto의 인터페이스를 만들어 read에서 상황에 따라 알맞은 인스턴스를 내보낼 수 있도록 하기 위한 인터페이스
public interface FindChannelResponseDto {
    UUID getId();

    UUID getOwnerId();

    Instant getLastMessageTime();

    List<UUID> getMembers();

    boolean getIsPublic();
    // default method
    // Java 8부터 도입됨
    // 인터페이스에 있는 구현 메서드
    // main에서 read() 사용 시 필요한 정보를 꺼내쓸 수 없는 문제를 해결하기 위해 디폴트 메서드 사용

    default String getCategory() {
        return null;
    }

    default String getName() {
        return null;
    }

    default String getExplanation() {
        return null;
    }
}
