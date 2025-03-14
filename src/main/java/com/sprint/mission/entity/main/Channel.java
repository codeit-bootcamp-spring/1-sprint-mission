package com.sprint.mission.entity.main;

import com.sprint.mission.entity.addOn.ReadStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

import static jakarta.persistence.CascadeType.*;

@Entity
@EqualsAndHashCode(of = {"channelType", "name"}, callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"channelType", "name", "description"})
@Getter @Builder
@Schema(description = "채널")
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity{

    private ChannelType channelType;
    private String name;
    private String description;

    @OneToMany(mappedBy = "channel", cascade = REMOVE, orphanRemoval = true)
    private List<ReadStatus> readStatus = new ArrayList<>();

    public Channel(String name, String description, ChannelType channelType) {
        this.name = name;
        this.channelType = channelType;
        this.description = description;
    }

    public Channel(ChannelType channelType) {
        this.channelType = channelType;
    }

    public void update(String newName, String newDescription) {
        this.name = newName;
        this.description = newDescription;
    }

    public boolean isPrivate() {
        if (this.channelType == ChannelType.PRIVATE) return true;
        else return false;
    }

    public static Channel createChannel(String name, String description, ChannelType channelType) {
        return new Channel(name, description, channelType);
    }


//    @AssertTrue(message = "채널 이름은 설명보다 짧아야 합니다.")
//    public boolean nameValidCheck() {
//        return this.name.length() < this.description.length();
//    }
}