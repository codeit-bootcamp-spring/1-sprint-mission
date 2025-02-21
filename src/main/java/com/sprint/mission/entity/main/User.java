package com.sprint.mission.entity.main;


import com.sprint.mission.dto.request.UserDtoForRequest;
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
    private final Instant createAt;
    private Instant updateAt;

    private String name;
    private String email;
    private String password;

    private UUID profileImgId;

    public User(String name, String password, String email, UUID profileImgId) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.name = name;
        this.password = password;
        this.email = email;
        this.profileImgId = profileImgId;
    }

    public static User createUserByRequestDto(UserDtoForRequest dto, UUID profileImgId){
        return new User(dto.getUsername(), dto.getPassword(), dto.getEmail(), profileImgId);
    }

    public void updateByRequestDTO(UserDtoForRequest requestDTO){
        this.name = requestDTO.getUsername();
        this.password = requestDTO.getPassword();
        this.email = requestDTO.getEmail();
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
        return "[" + name + "]" + " => email: " + email + ", password: " + password;
    }
}
