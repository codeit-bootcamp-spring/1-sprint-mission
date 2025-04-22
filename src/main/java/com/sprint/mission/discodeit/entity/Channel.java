package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.channel.ChannelTypeNotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

    @Column(name = "name", length = 100)
    private String serverName;    // 서버명

    @Column(name = "description", columnDefinition = "text")
    private String description; // 서버 소개

    @Column(name = "channel_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChannelType channelType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "fk_owner"))
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User owner;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelMember> members = new ArrayList<>();

    // 생성자
    public Channel(String serverName, ChannelType channelType, String description, User owner) {
        this.serverName = serverName;
        this.channelType = channelType;
        this.description = description;
        this.owner = owner;
    }

    public enum ChannelType {
        PUBLIC,
        PRIVATE;

        public static ChannelType fromString(String value) {
            if (StringUtils.isBlank(value)) {
                throw new ChannelTypeNotFoundException(value);
            }
            try {
                return ChannelType.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ChannelTypeNotFoundException(value);
            }
        }
    }

    // Setter (update)
    public void updateServerName(String serverName) {
        this.serverName = serverName;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public void addMembers(ChannelMember channelMember) {
        this.getMembers().add(channelMember);
    }

    public void addAllMembers(List<ChannelMember> channelMembers) {
        this.getMembers().addAll(channelMembers);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "serverName='" + serverName + '\'' +
                ", description='" + description + '\'' +
                ", channelType=" + channelType +
                '}';
    }
}
