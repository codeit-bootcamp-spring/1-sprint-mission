package com.sprint.mission.discodeit.binaryContent.entity;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BinaryContent {
	private UUID id;
	private Instant createdAt;

	private byte[] bytes;
	private String contentType;
	//파일 이름, 파일 크기 추가
	private String fileName;
	private long fileSize;

	public BinaryContent(byte[] bytes, String contentType, String fileName, long fileSize) {
		this.id = UUID.randomUUID();
		this.createdAt = Instant.now();
		this.bytes = bytes;
		this.contentType = contentType;
		this.fileName = fileName;
		this.fileSize = fileSize;
	}
}
