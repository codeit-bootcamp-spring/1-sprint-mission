package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.util.ChannelType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity implements Serializable {      // 채널 (게시판)

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private final User owner;       // 채널 주인

    @Column(name = "category")
    private String category;          // 채널 카테고리

    @Column(name = "name")
    private String name;              // 채널 이름

    @Column(name = "description")
    private String description;       // 채널 설명

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private final ChannelType type; // 공개 채널 여부

    @ManyToMany
    @JoinTable( // 조인 테이블 정의
            name = "channel_members",   // 조인 테이블 이름
            joinColumns = @JoinColumn(name = "channel_id"), // 외래키: 채널 ID
            inverseJoinColumns = @JoinColumn(name = "user_id")    // 외래키: 멤버 ID
    )
    private final List<User> members; // 멤버 목록

    @Transient  // DB에 저장되지 않게 함
    private Instant lastMessageTime;    // 마지막 메시지 시간


    // 생성자
    // public 채널
    public Channel(User owner, String name, String description) {

        // Channel 필드
        this.owner = owner;
        this.category = null;                       // 카테고리는 기본적으로 null로 설정
        validationAndSetName(name);
        this.description = description.trim();      // 앞뒤 공백 제거 후 저장

        // member
        this.members = new ArrayList<>();           // 텅빈 ArrayList로 초기화
        members.add(owner);                       // 기본적으로 멤버에 채널 주인 이름 넣어놓음

        this.type = ChannelType.PUBLIC;

        owner.addChannel(this);
    }

    // private 채널
    public Channel(User user) {
        // 공통 필드 초기화
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        // Channel 필드
        this.owner = user;
        this.category = null;                       // 카테고리는 기본적으로 null로 설정
        this.name = null;
        this.description = null;

        // member
        this.members = new ArrayList<>();           // 텅빈 ArrayList로 초기화
        members.add(user);                       // 기본적으로 멤버에 채널 주인 이름 넣어놓음

        this.type = ChannelType.PRIVATE;

        owner.addChannel(this);
    }


    // update 함수
    public void updateCategory(String category) {
        if (!category.isEmpty()){
            validationAndSetCategory(category);
        }
    }

    public void updateName(String name) {
        if (!name.isEmpty()){
            validationAndSetName(name);
        }
    }

    public void updateDescription(String description) {
        if (!description.isEmpty()) {
            this.description = description;
        }
    }

    public void addMember(User user) {
        if (!members.contains(user)) {
            this.members.add(user);
        }
    }

    public void deleteMember(UUID memberId) {
        User userToRemove = this.members.stream()
                .filter(member -> member.getId().equals(memberId))
                .findFirst()
                .orElse(null);

        if (userToRemove != null) {
            members.remove(userToRemove);
        }
    }

    public void updateLastMessageTime(Instant lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
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
                "owner=" + owner +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", explanation='" + description + '\'' +
                ", members=" + members +
                '}';
    }
}