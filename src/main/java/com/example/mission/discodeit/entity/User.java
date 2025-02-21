package com.example.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;  // 사용자 ID

    @Column(nullable = false, unique = true)
    private String username; // 사용자명

    @Column(nullable = false)
    private String email; // 이메일

    private UUID profileId; // 프로필 사진 ID (파일 UUID)

    private Boolean online = false; // 온라인 상태 (기본값 false)

    @CreationTimestamp
    private Instant createdAt; // 생성일

    @UpdateTimestamp
    private Instant updatedAt; // 업데이트일
}
