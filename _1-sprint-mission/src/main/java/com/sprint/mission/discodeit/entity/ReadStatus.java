package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델
//사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용
public class ReadStatus extends Common{
    private static final long serialVersionUID = 1L; //직렬화 버전
    @Id
    @GeneratedValue
    private UUID id;

    //User 와 대대일 관계
    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user; // 메시지를 읽은 사용자

    //Channel 과 다대일 관계
    @ManyToOne
    @JoinColumn(name="channel_id",nullable=false)
    private Channel channel; // 메시지를 읽은 채널

    private Instant lastReadTime; // 마지막으로 읽은 시각
    private boolean readStatus; // 읽은 상태

    public ReadStatus(User user, Channel channel, Instant lastReadTime, boolean readStatus) {
        super();
        this.user = user;
        this.channel = channel;
        this.lastReadTime = lastReadTime;
        this.readStatus = readStatus;
    }
    //마지막으로 읽은 시간 업데이트 및 읽은 상태 변경
    public void updateLastReadTime() {
        this.lastReadTime = Instant.now();
        this.readStatus = true;
        updateTimestamp();
    }
}
