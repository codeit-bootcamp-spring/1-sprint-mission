package com.sprint.mission.entity.main;


import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.service.dto.request.UserDtoForRequest;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;

    private String name;
    private String email;
    private String password;
    //private UserStatus userStatus;

    private final Instant createAt;
    private Instant updateAt;

    private final List<Channel> channels = new ArrayList<>();

    // 읽은거 STATUS
    private final ReadStatus readStatus; // 흠...

    public User(String name, String password, String email){
        this.name = name;
        this.password = password;
        this.email = email;
        this.readStatus = new ReadStatus();
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
    }

    public static User createUserByRequestDto(UserDtoForRequest dto){
        return new User(dto.getUsername(), dto.getPassword(), dto.getEmail());
    }


    public void setAll(String name, String password, String email) {
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void changeReadStatus(UUID channelId){
         readStatus.updateReadTime(channelId);
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password);
    }

    @Override
    public String toString() {
        return "[" + name + "]" + "=> email: " + email + ", password: " + password;
    }
}
