package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.Phone;
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
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "nickname", length = 50, nullable = false)
    private String nickname;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Embedded
    private Phone phone;

    @Column(name = "user_type", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(name = "is_active", nullable = false)
    private boolean status = true;

    @Column(name = "introduce")
    private String introduce;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "fk_profile"), nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private UserStatus userStatus;  // OneToOne은 fetchType이 Eager였다

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelMember> joinedChannels = new ArrayList<>();

    // 생성자
    public User(
            String username, String nickname, String email, String password,
            Phone phone, UserType userType, String introduce, BinaryContent profile
    ) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.userType = userType;
        this.introduce = introduce;
        this.profile = profile;
    }

    // Update
    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updatePhone(Phone phone) {
        this.phone = phone;
    }

    public void updateUserType(UserType userType) {
        this.userType = userType;
    }

    public void updateStatus(boolean status) {
        this.status = status;
    }

    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void updateProfile(BinaryContent profile) {
        this.profile = profile;
    }

    public void updateUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    // Enum: UserType
    public enum UserType {
        COMMON, // 일반 유저 (Default)
        BOT,    // 봇
        STAFF;   // 관리자

        public static UserType fromString(String value) {
            if (StringUtils.isBlank(value)) {
                throw new IllegalArgumentException("UserType은 null이 될 수 없습니다.");
            }
            try {
                return UserType.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("존재하지 않는 UserType: " + value);
            }
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email=" + email +
                ", phone=" + phone +
                ", userType=" + userType +
                ", status=" + status +
                ", introduce='" + introduce + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                '}';
    }
}
