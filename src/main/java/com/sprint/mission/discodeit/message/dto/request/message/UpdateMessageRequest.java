package com.sprint.mission.discodeit.message.dto.request.message;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

//기존에 첨부한 파일도 삭제시켜야되지 않을까 싶어 삭제해야되는 파일 id를 list로 받음
public record UpdateMessageRequest(
	String content,
	List<MultipartFile> attachments // 업데이트할 전체 첨부파일 목록
) {
}
