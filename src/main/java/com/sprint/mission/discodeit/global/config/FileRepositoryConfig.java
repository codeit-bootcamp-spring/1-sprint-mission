package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileReadStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileStorage;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

//@Configuration
//@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@RequiredArgsConstructor
public class FileRepositoryConfig {

    private final FileStorage fileStorage;

    @Bean
    public FileBinaryContentRepository fileBinaryContentRepository() {
        return new FileBinaryContentRepository(fileStorage);
    }

    @Bean
    public FileChannelRepository fileChannelRepository() {
        return new FileChannelRepository(fileStorage);
    }

    @Bean
    public FileMessageRepository fileMessageRepository() {
        return new FileMessageRepository(fileStorage);
    }

    @Bean
    public FileReadStatusRepository fileReadStatusRepository() {
        return new FileReadStatusRepository(fileStorage);
    }

    @Bean
    public FileUserRepository fileUserRepository() {
        return new FileUserRepository(fileStorage);
    }

    @Bean
    public FileUserStatusRepository fileUserStatusRepository() {
        return new FileUserStatusRepository(fileStorage);
    }
}
