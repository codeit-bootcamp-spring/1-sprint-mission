package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.Name;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.enums.ChannelType;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    /**
     * members, bannedUser도 같음.
     * members - 역할, 가입 시간, 서버 닉네임, UUID
     * bannedUser - UUID, 차단된 이유, 차단 기간?
     */
    private UUID id;    // 고유 번호
    private UUID ownerId;     // 서버 주인
    private Name serverName;    // 서버명
    private String description; // 서버 소개
    private ChannelType channelType;
    private List<UUID> members;     // 채널 가입자 리스트 - 근데 이건 나중에 객체로 빼는게 좋지 않을까..? 관리하기가 좀
    private List<UUID> bannedUser;  // 차단 사용자 리스트 - 근데 이건 나중에 객체로 빼는게 좋지 않을까..?
    private boolean status;     // 서버가 활성 상태인지 비활성화 상태인지 상태
    private Instant createdAt;
    private Instant updatedAt;

    // 생성자
    public Channel(ChannelDTO.request channelReqDTO) {
        id = UUID.randomUUID();
        createdAt = Instant.now();
        this.ownerId = channelReqDTO.owner();
        this.serverName = new Name(channelReqDTO.serverName());
        this.channelType = channelReqDTO.channelType();
        this.description = channelReqDTO.description();
        members = new ArrayList<>();
        bannedUser = new ArrayList<>();
        this.status = true;  // 기본 생성 시 활성화 상태
    }

    // 중복 확인

    // Setter (update)
    public void updateOwner(UUID userId) {
        this.ownerId = userId;
        setUpdatedAt();
    }

    public void updateServerName(String name) {
        this.serverName.setName(name);
        setUpdatedAt();
    }

    public void updateDescription(String content) {
        this.description = content;
        setUpdatedAt();
    }

    public void updateStatus(boolean status) {
        this.status = status;
        setUpdatedAt();
    }

    public void addMember(UUID member) {
        this.members.add(member);
        setUpdatedAt();
    }

    public void removeMember(UUID member) {
        this.members.remove(member);
        setUpdatedAt();
    }

    public void addBannedUser(UUID bannedUser) {
        this.bannedUser.add(bannedUser);
        setUpdatedAt();
    }

    public void removeBannedUser(UUID bannedUser) {
        this.bannedUser.remove(bannedUser);
        setUpdatedAt();
    }

    void setUpdatedAt() {
        updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Channel {" +
                "id=" + id +
                ", owner=" + ownerId +
                ", serverName=" + serverName +
                ", description='" + description + '\'' +
                '}';
    }
}
