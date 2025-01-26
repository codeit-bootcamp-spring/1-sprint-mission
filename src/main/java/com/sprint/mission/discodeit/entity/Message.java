package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {                  // 메시지 (게시물)

    @Serial
    private static final long serialVersionUID = 1L;

    // 공통 필드
    private final UUID id;              // pk
    private final Long createdAt;       // 생성 시간
    private Long updatedAt;             // 수정 시간

    private final UUID channelId;       // 메시지가 속해있는 채널
    private final UUID writerId;        // 작성자 id
    private String context;             // 메시지 내용

    // 생성자
    public Message(Channel channel, String context, UUID writerId){
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();

        this.channelId = channel.getId();
        validationAndSetContext(context);
        this.writerId = writerId;
    }


    // Getter 함수
    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public UUID getChannelId() {
        return channelId;
    }
  
    public UUID getWriterId() {
        return writerId;
    }

    public String getContext() {
        return context;
    }


    // update 함수
    public void updateContext(String context) {
        validationAndSetContext(context);
        updateUpdateAt();
    }

    public void updateUpdateAt(){
        this.updatedAt = System.currentTimeMillis();
    }


    // 메시지 내용 유효성 검사 및 세팅
    private void validationAndSetContext(String context) {
        if (context == null || context.isBlank()) {
            throw new IllegalArgumentException("메시지 내용을 입력해주세요.");
        }

        context = context.trim();

        this.context = context;
    }
}