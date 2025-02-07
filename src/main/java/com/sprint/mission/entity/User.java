package com.sprint.mission.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String firstId;

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
        this.firstId = id.toString().split("-")[0];
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
    }

    public void setAll(String name, String password, String email) {
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void changeReadStatus(Channel channel){
        if (channels.contains(channel)){
            readStatus.updateReadTime(channel);
        }
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
        return "[" + firstId + "]" + "'" + name + "'";
    }
}
