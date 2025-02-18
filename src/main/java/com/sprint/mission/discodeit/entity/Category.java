package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private final UUID channelId;
    private String category;

    // 채널 처음 생성 시 카테고리는 null로 초기화
    public Category(Channel channel) {       // Channel에 대한 의존성 주입

        this.id = UUID.randomUUID();

        this.channelId = channel.getId();
        this.category = null;
    }

    // 카테고리 수정
    public void updateCategory(String category) {

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("카테고리명을 입력해주세요.");
        }

        category = category.trim();

        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }
}
