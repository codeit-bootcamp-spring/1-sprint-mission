package com.sprint.mission.discodeit.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Name
 */
@Getter
@Setter
public class Name implements Serializable {
    private String name;

    public Name(String name) {
        this.name = name;
    }
}
