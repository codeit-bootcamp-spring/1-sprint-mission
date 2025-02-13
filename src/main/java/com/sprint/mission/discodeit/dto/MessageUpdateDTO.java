package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageUpdateDTO {
    private UUID id;             // 메시지 ID (수정할 대상)
    private String content;      // 수정할 메시지 내용
    private UUID attachmentId;   // 추가 또는 변경할 첨부파일 (선택 사항)
}
