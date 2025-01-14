package com.sprint.mission.discodeit.entity;

// 채팅 사용자 엔티티
public class User extends BaseEntity {
    private String name; // 사용자 이름

    // 사용자 이름을 초기화하는 생성자
    public User(String name) {
        super();
        this.name = name;
    }

    // 사용자 이름을 반환
    public String getName() {
        return name;
    }

    // 사용자 이름을 설정하고 수정 시간을 업데이트
    public void setName(String name) {
        this.name = name;
        updateTimestamp();
    }
}
