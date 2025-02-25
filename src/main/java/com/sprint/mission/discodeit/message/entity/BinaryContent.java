package com.sprint.mission.discodeit.message.entity;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BinaryContent {
	private UUID id;
	private Instant createdAt;
	private UUID authorId;
	//message없이 프로필 이미지만 저장하는 경우 때문에 optional로 처리해야될까...?
	private UUID messageId;
	private byte[] content;
	private String contentType;
	//파일 이름, 파일 크기 추가
	private String fileName;
	private long fileSize;
	private BinaryContentType binaryContentType;

	// 프로필 이미지용 생성자
	public static BinaryContent createProfileImage(UUID authorId, MultipartFile file) throws IOException {
		return new BinaryContent(
			UUID.randomUUID(),
			Instant.now(),
			authorId,
			null,  // messageId는 null
			file.getBytes(),
			file.getContentType(),
			file.getOriginalFilename(),
			file.getSize(),
			BinaryContentType.PROFILE_IMAGE
		);
	}

	// 메시지 첨부 파일용 생성자
	public static BinaryContent createMessageAttachment(UUID authorId, UUID messageId, MultipartFile file) throws
		IOException {
		return new BinaryContent(
			UUID.randomUUID(),
			Instant.now(),
			authorId,
			messageId,
			file.getBytes(),
			file.getContentType(),
			file.getOriginalFilename(),
			file.getSize(),
			BinaryContentType.MESSAGE_ATTACHMENT
		);
	}

	// private 생성자
	private BinaryContent(UUID id, Instant createdAt, UUID authorId, UUID messageId, byte[] content, String contentType,
		String fileName, long fileSize, BinaryContentType binaryContentType) {
		this.id = id;
		this.createdAt = createdAt;
		this.authorId = authorId;
		this.messageId = messageId;
		this.content = content;
		this.contentType = contentType;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.binaryContentType = binaryContentType;
	}
}
