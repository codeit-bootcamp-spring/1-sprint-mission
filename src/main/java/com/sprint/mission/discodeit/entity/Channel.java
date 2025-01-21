package com.sprint.mission.discodeit.entity;

// 채팅 채널 엔티티
public class Channel extends BaseEntity {
    private String title; // 채널 제목

    // 채널 제목을 초기화하는 생성자
    public Channel(String title) {
        super();
        this.title = title;
    }

    // 채널 제목을 반환
    public String getTitle() {
        return title;
    }

    // 채널 제목을 설정하고 수정 시간을 업데이트
    public void setTitle(String title) {
        this.title = title;
        updateTimestamp();
    }
}
