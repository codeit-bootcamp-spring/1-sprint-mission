package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String username;
    private String email;
    private UUID profileImageId;
    private String password;
    private boolean online;
    private Instant lastActive; // ✅ 마지막 활동 시간 추가

    // ✅ 기존 생성자 (lastActive 기본값: null)
    public User(UUID id, String username, String email, UUID profileImageId, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImageId = profileImageId;
        this.password = password;
        this.online = false;
        this.lastActive = Instant.now();
    }

    public User(String username, String email, UUID profileImageId, String password) {
        this(UUID.randomUUID(), username, email, profileImageId, password);
    }

    // ✅ 새로운 생성자 추가 (email과 profileImageId 없이 사용자 등록 가능)
    public User(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = ""; // 기본값 설정 (필요시 변경 가능)
        this.profileImageId = null; // 프로필 이미지 기본값
        this.lastActive = null;
    }

    // ✅ **마지막 활동 시간 업데이트 메서드 추가**
    public void setLastActive(Instant lastActive) {
        this.lastActive = lastActive;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
