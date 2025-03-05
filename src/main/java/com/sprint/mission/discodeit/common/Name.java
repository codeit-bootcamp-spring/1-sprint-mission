package com.sprint.mission.discodeit.common;

import com.fasterxml.jackson.annotation.JsonValue;
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
    @JsonValue
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
