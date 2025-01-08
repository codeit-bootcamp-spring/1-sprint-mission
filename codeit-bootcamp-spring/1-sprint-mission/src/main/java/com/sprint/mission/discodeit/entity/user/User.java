package com.sprint.mission.discodeit.entity.user;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;

public class User extends AbstractUUIDEntity {


    private UserName name;


    public User(UserName name) {
        this.name = name;
    }

    public String getName() {
        return name.toString();
    }

}

