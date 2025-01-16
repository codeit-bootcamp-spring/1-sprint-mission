package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private String title;


    public Channel(String title){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.title = title;
    }

    public UUID getId() {
        return id;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public void updateId(UUID id) {
        this.id = id;
    }
    public void updateCreatedAt(Long CreatedAt) {
        this.createdAt = createdAt;
    }


    public void updateTitle(String title) {
        this.title = title;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getTitle() {
        return title;
    }



    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return "Title : " + title + "/ createdAt : " + simpleDateFormat.format(createdAt) + "updatedAt : " + updatedAt;
    }
}
