package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Getter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private final String name;
    private String email;
    private String password;
    private String accountId;
    private UUID binaryContentId;


    public User(String name, String email,String iD ,String password){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.name = name;
        this.email = email;
        this.accountId = iD;
        this.password = password;
    }
    public User(String name, String email,String iD ,String password,UUID binaryContentId ){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.name = name;
        this.email = email;
        this.accountId = iD;
        this.password = password;
        this.binaryContentId = binaryContentId;
    }


    public void update(String email, String iD, String password){
        this.email = email;
        this.accountId = iD;
        this.password = password;
        updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "User{\n" +
                "UUID : " + id +
                ", \ncreatedAt : " + createdAt +
                ", \nupdatedAt : " + updatedAt +
                ", \nname : " + name +
                ", \nemail : " + email +
                ", \nid : " + accountId + "\n}";
    }

}
