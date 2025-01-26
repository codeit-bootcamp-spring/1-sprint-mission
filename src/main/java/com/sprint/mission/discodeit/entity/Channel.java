package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {      // 채널 (게시판)

    @Serial
    private static final long serialVersionUID = 1L;

    // 공통 필드
    private final UUID id;            // pk
    private final Long createdAt;     // 생성 시간
    private Long updatedAt;           // 수정 시간

    private final UUID ownerId;       // 채널 주인
    private final Category category;  // 채널 카테고리
    private String name;              // 채널 이름
    private String explanation;       // 채널 설명
    private final List<UUID> members;       // 멤버 목록


    // 생성자
    public Channel(User user, String name, String explanation) {
        // 공통 필드 초기화
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();

        // Channel 필드
        this.ownerId = user.getId();
        validationAndSetName(name);
        this.explanation = explanation.trim();      // 앞뒤 공백 제거 후 저장

        // member
        this.members = new ArrayList<>();           // 텅빈 ArrayList로 초기화
        members.add(ownerId);                       // 기본적으로 멤버에 채널 주인 이름 넣어놓음

        this.category = new Category(this);  // 카테고리는 기본적으로 null로 설정
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
        return category.toString();
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
    public void updateCategory(String category) {
        this.category.updateCategory(category);
        updateUpdateAt();
    }

    public void updateName(String name) {
        validationAndSetName(name);
        updateUpdateAt();
    }

    public void updateExplanation(String explanation) {
        this.explanation = explanation;
        updateUpdateAt();
    }

    public void addMember(UUID memberId) {
        this.members.add(memberId);
        updateUpdateAt();
    }

    public void deleteMember(UUID memberId) {
        this.members.remove(memberId);
        updateUpdateAt();
    }

    public void updateUpdateAt() {
        this.updatedAt = System.currentTimeMillis();
    }

    // 채널 이름 유효성 검사 및 세팅
    private void validationAndSetName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("채널명을 입력해주세요.");
        }

        name = name.trim();

        this.name = name;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "ownerId=" + ownerId +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", explanation='" + explanation + '\'' +
                ", members=" + members +
                '}';
    }
}