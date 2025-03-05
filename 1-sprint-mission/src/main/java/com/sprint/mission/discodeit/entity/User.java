package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = -4674437962852345619L;
    private UUID id = UUID.randomUUID();
    private String name;
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private String password;
    private String email;
    private Boolean isContainContent;

    SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");

    @Override
    public String toString() {
        return "User\n" +
                "id = " + id + '\n' +
                "name = " + name + '\n' +
                "email = " + email + '\n' +
                "createdAt = " + (createdAt) + '\n' +
                "updatedAt = " + (updatedAt)+ '\n';
    }

}
