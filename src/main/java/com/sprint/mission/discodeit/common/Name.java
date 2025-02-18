package com.sprint.mission.discodeit.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Name
 */
@Getter
@Setter
@AllArgsConstructor
public class Name implements Serializable {
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
