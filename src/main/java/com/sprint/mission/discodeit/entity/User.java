package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String username;
    private String email;
    private UUID profileImageId;
    private String password;

    // ✅ 기존 생성자
    public User(UUID id, String username, String email, UUID profileImageId, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImageId = profileImageId;
        this.password = password;
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
    }
}
