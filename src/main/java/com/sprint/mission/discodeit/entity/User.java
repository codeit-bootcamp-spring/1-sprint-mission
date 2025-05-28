package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseUpdatableEntity {

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "profile_id") // table의 'profile_id' 컬럼
    private BinaryContent profileImage;

    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.ROLE_USER;
    }

    public static User of(String name, String email, String password) {
        return new User(name, email, password);
    }

    public void updateName(String name) {
        if (name == null) {
            return;
        }
        this.name = name;
        this.updateUpdatedAt();
    }

    public void updateEmail(String email) {
        if (email == null) {
            return;
        }
        this.email = email;
        this.updateUpdatedAt();
    }

    public void updatePassword(String password) {
        if (password == null) {
            return;
        }
        this.password = password;
        this.updateUpdatedAt();
    }

    public void updateRole(Role role) {
        if (role == null) {
            return;
        }
        this.role = role;
    }

    public void updateProfileImage(BinaryContent profileImage) {
        this.profileImage = profileImage;
        this.updateUpdatedAt();
    }

    public void updateUserInfo(String name, String email, String password) {
        this.updateName(name);
        this.updateEmail(email);
        this.updatePassword(password);
    }
}
