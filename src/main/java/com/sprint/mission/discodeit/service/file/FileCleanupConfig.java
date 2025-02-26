package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.repository.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class FileCleanupConfig {
    private static final Logger logger = LoggerFactory.getLogger(FileCleanupConfig.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @PostConstruct
    public void cleanupFiles() {
        // .discodeit 디렉토리 경로
        String dirPath = ".discodeit";

        try {
            // 디렉토리가 존재하는 경우
            File directory = new File(dirPath);
            if (directory.exists()) {
                // 디렉토리 내의 모든 파일 가져오기
                File[] files = directory.listFiles();
                if (files != null) {
                    // 각 파일 삭제
                    for (File file : files) {
                        if (file.isFile()) {
                            boolean deleted = file.delete();
                            if (!deleted) {
                                logger.warn("Failed to delete file: " + file.getName());
                            }
                        }
                    }
                }

                // 디렉토리 자체도 삭제
                boolean deleted = directory.delete();
                if (!deleted) {
                    logger.warn("Failed to delete directory: " + dirPath);
                }
            }

            // 새로운 디렉토리 생성
            boolean created = new File(dirPath).mkdirs();
            if (!created) {
                logger.warn("Failed to create directory: " + dirPath);
            }

            // 저장소 데이터 초기화
            userRepository.clearData();
            userStatusRepository.clearData();
            channelRepository.clearData();
            messageRepository.clearData();
            readStatusRepository.clearData();
            binaryContentRepository.clearData();
        } catch (Exception e) {
            logger.error("Error while cleaning up .discodeit directory", e);
            throw new RuntimeException("Failed to initialize application", e);
        }
    }
}