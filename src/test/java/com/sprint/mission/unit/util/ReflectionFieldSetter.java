package com.sprint.mission.unit.util;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.util.ReflectionTestUtils.*;

public class ReflectionFieldSetter {

    public Object settingFieldValue(Object target) {
        setField(target, "id", UUID.randomUUID());
        setField(target, "createdAt", Instant.now());




        return target;
    }
}
