package com.sprint.mission.discodeit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)  // JUnit5 + Spring 테스트 통합
@SpringBootTest
class DiscodeitApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("✅ DiscodeitApplicationTests 실행 완료!");
    }
}
