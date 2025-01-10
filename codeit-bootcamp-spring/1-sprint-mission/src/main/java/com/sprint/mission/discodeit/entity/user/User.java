package com.sprint.mission.discodeit.entity.user;

import static com.sprint.mission.discodeit.entity.common.Status.MODIFIED;
import static com.sprint.mission.discodeit.entity.common.Status.UNREGISTERED;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;

public class User extends AbstractUUIDEntity {

    private UserName name;

    public User(UserName name) {
        this.name = name;
    }

    public String getName() {
        return name.getName();
    }

    public void changeName(String newName) {
        this.name = name.changeName(newName);
        updateStatusAndUpdateAt(MODIFIED);
    }

    public void UnRegister() {
        updateStatusAndUpdateAt(UNREGISTERED);
    }

}

