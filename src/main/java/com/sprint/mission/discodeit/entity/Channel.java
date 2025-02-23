package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class Channel implements Serializable {      // 채널 (게시판)

    @Serial
    private static final long serialVersionUID = 1L;

    // 공통 필드
    private final UUID id;            // pk
    private final Instant createdAt;  // 생성 시간
    private Instant updatedAt;        // 수정 시간

    private final UUID ownerId;       // 채널 주인
    private String category;          // 채널 카테고리
    private String name;              // 채널 이름
    private String explanation;       // 채널 설명
    private final List<UUID> members; // 멤버 목록
    private final Map<UUID, ReadStatus> readStatuses;    // 멤버별 읽음 상태
    private Instant lastMessageTime;
    private final boolean isPublic; // 공개 채널 여부

    // 생성자
    // public 채널
    public Channel(UUID ownerId, String name, String explanation) {
        // 공통 필드 초기화
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        // Channel 필드
        this.ownerId = ownerId;
        this.category = null;                       // 카테고리는 기본적으로 null로 설정
        validationAndSetName(name);
        this.explanation = explanation.trim();      // 앞뒤 공백 제거 후 저장

        // member
        this.members = new ArrayList<>();           // 텅빈 ArrayList로 초기화
        members.add(ownerId);                       // 기본적으로 멤버에 채널 주인 이름 넣어놓음

        // readStatus
        this.readStatuses = new HashMap<>();
        readStatuses.put(ownerId, new ReadStatus(this.ownerId, this.id));    // 채널주 readStatus 넣어놓음

        this.isPublic = true;
    }

    // private 채널
    public Channel(UUID userId) {
        // 공통 필드 초기화
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        // Channel 필드
        this.ownerId = userId;
        this.category = null;                       // 카테고리는 기본적으로 null로 설정
        this.name = null;
        this.explanation = null;

        // member
        this.members = new ArrayList<>();           // 텅빈 ArrayList로 초기화
        members.add(userId);                       // 기본적으로 멤버에 채널 주인 이름 넣어놓음

        // readStatus
        this.readStatuses = new HashMap<>();
        readStatuses.put(ownerId, new ReadStatus(this.ownerId, this.id));    // 채널주 readStatus 넣어놓음

        this.isPublic = false;
    }



    // update 함수
    public void updateCategory(String category) {
        if (!category.isEmpty()){
            validationAndSetCategory(category);
            updateUpdateAt();
        }
    }

    public void updateName(String name) {
        if (!name.isEmpty()){
            validationAndSetName(name);
            updateUpdateAt();
        }
    }

    public void updateExplanation(String explanation) {
        if (!explanation.isEmpty()){
            this.explanation = explanation;
            updateUpdateAt();
        }
    }

    public void addMember(UUID memberId) {
        this.members.add(memberId);
        updateUpdateAt();
    }

    public void deleteMember(UUID memberId) {
        this.members.remove(memberId);
        updateUpdateAt();
    }

    public void updateReadStatus(UUID memberId) {
        if (members.contains(memberId)) {
            this.readStatuses.get(memberId).updateLastReadTime();
        } else {
            this.readStatuses.put(memberId, new ReadStatus(memberId, this.id));
        }
        updateUpdateAt();
    }

    public void updateLastMessageTime(Instant lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
        updateUpdateAt();
    }

    public void updateUpdateAt() {
        this.updatedAt = Instant.now();
    }


    // 채널 이름 유효성 검사 및 세팅
    private void validationAndSetName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("채널명을 입력해주세요.");
        }

        name = name.trim();

        this.name = name;
    }

    // 카테고리 유효성 검사 및 세팅
    private void validationAndSetCategory(String category) {

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("채널명을 입력해주세요.");
        }

        category = category.trim();

        this.category = category;
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