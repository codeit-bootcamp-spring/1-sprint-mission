package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class BinaryContent {
	private final UUID id;
	private final Instant createdAt;
	private final UUID authorId;
	//message없이 프로필 이미지만 저장하는 경우 때문에 optional로 처리해야될까...?
	private final UUID messageId;
	private final byte[] content;
	private final String contentType;
	//파일 이름, 파일 크기 추가
	private final String fileName;
	private final long fileSize;
	private final BinaryContentType binaryContentType;

	//프로필 이미지용 생성자
	public BinaryContent(UUID id, Instant createdAt, UUID authorId, UUID messageId, byte[] content, String contentType,
		String fileName, long fileSize) {
		this.id = id;
		this.createdAt = createdAt;
		this.authorId = authorId;
		this.messageId = messageId;
		this.content = content;
		this.contentType = contentType;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.binaryContentType = BinaryContentType.PROFILE_IMAGE;
	}

	//메시지 첨부 파일용 생성자
	public BinaryContent(Instant createdAt, UUID authorId, UUID messageId, byte[] content, String contentType,
		String fileName, long fileSize) {
		this.id = UUID.randomUUID();
		this.createdAt = Instant.now();
		this.authorId = authorId;
		this.messageId = messageId;
		this.content = content;
		this.contentType = contentType;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.binaryContentType = BinaryContentType.MESSAGE_ATTACHMENT;
	}
}
