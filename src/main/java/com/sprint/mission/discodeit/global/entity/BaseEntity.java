package com.sprint.mission.discodeit.global.entity;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class BaseEntity {
	private final UUID id;
	private final Instant createdAt;
	private Instant updatedAt;

	// 기본 생성자: id와 createdAt 초기화
	public BaseEntity() {
		this.id = UUID.randomUUID();
		this.createdAt = Instant.now();
		this.updatedAt = this.createdAt;
	}

	public void updateTime() {
		this.updatedAt = Instant.now();
	}
}
