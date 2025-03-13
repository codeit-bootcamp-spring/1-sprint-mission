package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserFindDTO{

    private final UUID id;
    private final String name;
    private final String email;
    private final String filePath;
    private final boolean isOnline;

    public UserFindDTO(User user) {
        this.id = user.getId();
        this.name=user.getUserName();
        this.email=user.getEmail();
        this.filePath=user.getBinaryContent().getFilePath();
        this.isOnline=user.getUserStatus().isOnline();
    }

    @Override
    public String toString() {
        return "User[Name: " + this.getName() +
                " Email: " + this.getEmail() +
                " FilePath: " + this.getFilePath()+
                " isOnline: " + this.isOnline +
                " ID: " + this.id + "]";
    }


}
