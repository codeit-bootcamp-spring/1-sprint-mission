package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {      // 채널 (게시판)
    // 공통 필드
    private final UUID id;            // pk
    private final Long createdAt;     // 생성 시간
    private Long updatedAt;           // 수정 시간

    private final UUID ownerId;       // 채널 주인
    private String category;          // 채널 카테고리
    private String name;              // 채널 이름
    private String explanation;       // 채널 설명
    private List<UUID> members;       // 멤버 목록


    // 생성자
    public Channel(UUID ownerId, String category, String name, String explanation) {
        // 공통 필드 초기화
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        
        // Channel 필드
        this.ownerId = ownerId;
        this.category = category;
        this.name = name;
        this.explanation = explanation;
        this.members = new ArrayList<>();   // 텅빈 ArrayList로 초기화
        members.add(ownerId);     // 기본적으로 멤버에 채널 주인 이름 넣어놓음
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

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getExplanation() {
        return explanation;
    }

    public List<UUID> getMembers() {
        return members;
    }



    // update 함수
    public void updateCategory(String category){
        this.category = category;
        updateUpdateAt();
    }

    public void updateName(String name) {
        this.name = name;
        updateUpdateAt();
    }

    public void updateExplanation(String explanation) {
        this.explanation = explanation;
        updateUpdateAt();
    }

    public void updateMembers(List<UUID> members) {
        this.members = members;
        updateUpdateAt();
    }

    public void updateUpdateAt(){
        this.updatedAt = System.currentTimeMillis();
    }
}