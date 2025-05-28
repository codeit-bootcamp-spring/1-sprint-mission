package com.spirnt.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spirnt.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User extends BaseUpdatableEntity {

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @JsonIgnore // 비밀번호 노출 방지
    private String password;

    // on delete set null 설정
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private BinaryContent profile;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Role role;


    public User(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = Role.ROLE_USER;
    }

    public void setProfile(BinaryContent binaryContent) {
        this.profile = binaryContent;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void update(String name, String email, String password, BinaryContent profile) {
        if (name != null && !name.equals(this.username)) {
            this.username = name;
        }
        if (email != null && !email.equals(this.email)) {
            this.email = email;
        }
        if (password != null && !password.equals(this.password)) {
            this.password = password;
        }
        if (profile != null && !profile.equals(this.profile)) {
            this.profile = profile;
        }
    }

    // UUID만으로 객체를 비교하기 위해 추가
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return Objects.equals(this.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
