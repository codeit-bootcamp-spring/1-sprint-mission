package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter @Setter
@Entity 
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
    
    @Builder
    public User(String name, String email, String password, boolean online, byte[] profileImage, BinaryContent profile, 
                List<ReadStatus> readStatuses, List<Message> messages, List<UserStatus> userStatuses, UUID id) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.online = online;
        this.profileImage = profileImage;
        this.profile = profile;
        
        if (readStatuses != null) {
            this.readStatuses = readStatuses;
        }
        
        if (messages != null) {
            this.messages = messages;
        }
        
        if (userStatuses != null) {
            this.userStatuses = userStatuses;
        }
        
        if (id != null) {
            this.setId(id);
        }
    }
}