package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UUIDTest {

    @Test
    void uuidTest() {
        var uuid = UUID.randomUUID();
        var targetUUID = UUID.fromString(uuid.toString());

        var retsult = uuid.equals(targetUUID);

        assertThat(retsult).isTrue();
    }

}
