package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter @Setter
@Entity @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseUpdatableEntity {

    private String name;

    private String email;

    private String password;

    private boolean online;
    @Lob
    private byte[] profileImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadStatus> readStatuses = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStatus> userStatuses = new ArrayList<>();


    public void addUserStatus(UserStatus status) {
        userStatuses.add(status);
        status.setUser(this);
    }

    public void removeUserStatus(UserStatus status) {
        userStatuses.remove(status);
        status.setUser(null);
    }
}