package com.sprint.mission.discodeit.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class Common {
    //필드
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    Long unixTime = System.currentTimeMillis();
    Date date = new Date(unixTime*1000);
    //생성자
    public Common(){
        this.id=UUID.randomUUID();
        this.createdAt=System.currentTimeMillis();
        this.updatedAt=createdAt;
    }
    //Getter
    public UUID getId(){
        return id;
    }
    public Long getCreatedAt(){
        return createdAt;
    }
    public Long getUpdatedAt(){
        return updatedAt;
    }

    //날짜 변환 메소드
    public String formatTimestamp(Long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    public String getFormattedCreatedAt() {
        return formatTimestamp(createdAt);
    }

    public String getFormattedUpdatedAt() {
        return formatTimestamp(updatedAt);
    }

    //update 메소드
    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
