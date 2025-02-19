package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
public class User extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;

    private transient String password;


    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }




    public void update(String name, String email, String password) {
        boolean anyValueUpdated = false;
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            anyValueUpdated = true;
        }
        if (email != null && !email.equals(this.email)) {
            this.email = email;
            anyValueUpdated = true;
        }
        if (email != null && !email.equals(this.password)) {
            this.password = email;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updateClass(Instant.now());
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.name + '\'' +
                ", email='" + this.email + '\'' +
                '}';
    }

    // UUID만으로 객체를 비교하기 위해 추가
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(this.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
