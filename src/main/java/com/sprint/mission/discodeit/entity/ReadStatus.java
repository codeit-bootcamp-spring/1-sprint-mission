package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable, Entity {

    private UUID id;
    private Instant createdAt;
    @Setter private Instant updatedAt;

    //todo 엔티티 패키지에 스테이터스 클래스들이 같이 들어가는게 좀 부자연스럽지않나? (이 의문들은 모두 유저스테이터스도 포함)
    //todo 순환참조 피하기 위해 스테이터스에 연관된 채널과 유저가 누군지를 넣진 않아야할것같은데 그럼 나중에 문제 없으려나?

    //todo 리드스테이터스란 채널별로 채널에 속한 멤버들마다 하나의 객체가 생성되며,
    // 멤버가 그 채널에 들어올 때 업데이트앳이 갱신되는건가? id는 유저, 채널과 별개로 이 객체 자체의 id인건가?

    // 생성자
    private ReadStatus() {
        id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // 업데이트시간 변경
    public void setUpdatedAt() {
        updatedAt = Instant.now();
    }
}
