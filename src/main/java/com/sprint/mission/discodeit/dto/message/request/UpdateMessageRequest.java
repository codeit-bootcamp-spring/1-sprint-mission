package com.sprint.mission.discodeit.dto.message.request;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

//기존에 첨부한 파일도 삭제시켜야되지 않을까 싶어 삭제해야되는 파일 id를 list로 받음
public record UpdateMessageRequest(
	String content,
	List<UUID> attachmentsToRemove,    // 삭제할 첨부파일 ID 목록
	List<MultipartFile> attachmentsToAdd // 새로 추가할 첨부파일 목록
) {
}
