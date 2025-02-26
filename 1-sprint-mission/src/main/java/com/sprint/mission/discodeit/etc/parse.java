package com.sprint.mission.discodeit.etc;

import java.util.UUID;

public class parse {
    protected Object parseUUIDOrString(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return value;
        }
    }
}
