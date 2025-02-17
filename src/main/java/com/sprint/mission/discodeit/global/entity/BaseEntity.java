package com.sprint.mission.discodeit.global.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
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
