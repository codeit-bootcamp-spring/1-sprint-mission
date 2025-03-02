package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.springframework.boot.autoconfigure.domain.EntityScan;

// TODO JPA 위해서는 @NoArgsConstructor로 기본생성자 필요 -> final 못 씀 but 파라미터 전달로 객체생성해줘야하니까
// TODO @AllArgsConstructor, @Builder 쓰기 -> @Builder는 원하는 필드의 파라미터만 전달해서 객체 생성 가능
// TODO 아니면 그냥 파라미터 생성자는 롬복쓰지말고 직접 만들어주기..

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// TODO찐 : JPA에서 기본생성자 필수, protected 이유 : proxy..?
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id = UUID.randomUUID();
  private final Instant createdAt = Instant.now();

  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;

  private UUID userId;  // User 참조
  private UUID messageId;  // Message 참조

}
