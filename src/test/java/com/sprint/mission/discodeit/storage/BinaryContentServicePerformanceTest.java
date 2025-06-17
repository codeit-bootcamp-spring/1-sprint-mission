package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BinaryContentServicePerformanceTest {

    @Autowired
    private BinaryContentService binaryContentService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void 파일_저장_시_동기_비동기_성능_차이_확인() {

        User user = userRepository.saveAndFlush(
            User.createUserWithoutProfile("test", "email", "pw"));
        UUID requestId = UUID.randomUUID();

        MockMultipartFile testFile = new MockMultipartFile(
            "testFile",
            "test.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "테스트 파일 내용".getBytes()
        );

        int iterations = 5;
        for (int i = 0; i < iterations; i++) {
            binaryContentService.save(testFile, user.getId(), requestId);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            binaryContentService.saveSync(testFile);
        }

        Timer asyncTimer = meterRegistry.find("file.upload.async").timer();
        Timer syncTimer = meterRegistry.find("file.upload.sync").timer();
        double asyncMeanTime = asyncTimer.mean(TimeUnit.MILLISECONDS);
        double syncMeanTIme = syncTimer.mean(TimeUnit.MILLISECONDS);

        System.out.println("============= 성능 비교 결과 확인 =================");
        System.out.println("비동기 평균 응답 시간 : " + asyncMeanTime + "ms");
        System.out.println("동기 평균 응답 시간 : " + syncMeanTIme + "ms");
        System.out.println("비동기 최대 시간: " + asyncTimer.max(TimeUnit.MILLISECONDS) + "ms");
        System.out.println("동기 최대 시간: " + syncTimer.max(TimeUnit.MILLISECONDS) + "ms");
        System.out.println("차이: " + (syncMeanTIme - asyncMeanTime) + "ms");
    }

}
