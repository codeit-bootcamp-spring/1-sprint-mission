package com.sprint.mission.entity.main;

import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.addOn.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@EqualsAndHashCode(of = {"username", "email", "password"}, callSuper = true)
@ToString(of = {"username", "email", "password", "profile"})  // callSuper 제거 및 id 등 직접 명시
@Getter //@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Schema(description = "유저")
@Table(name = "users")
public class User extends BaseUpdatableEntity{

    private String username;
    private String email;
    private String password;

    //변경가능하니
    @OneToOne(fetch = LAZY, cascade = REMOVE, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @OneToOne(fetch = LAZY, mappedBy = "user", cascade = ALL)
    private UserStatus status;

    // REMOVE => user가 삭제되면 readStatus도 삭제됨
    @OneToMany(mappedBy = "user", cascade = REMOVE, orphanRemoval = true)
    private List<ReadStatus> readStatus = new ArrayList<>();

    public User(String username, String password, String email, BinaryContent profile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profile = profile;
    }

//    public User(String username, String password, String email) {
//        this.username = username;
//        this.password = password;
//        this.email = email;
//    }

    public void update(String newName, String newPassword, String newEmail) {
        this.username = newName;
        this.password = newPassword;
        this.email = newEmail;
    }

//    public Optional<BinaryContent> getProfile() { // mapping은 null 체크 해줌
//        return Optional.ofNullable(profile);
//    }
}
