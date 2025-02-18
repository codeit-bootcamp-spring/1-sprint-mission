package com.sprint.mission.discodeit.repository.config;

import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinaryContentRepositoryConfig {
    @Bean
    public BinaryContentRepository binaryContentRepository(
            @Value("${discodeit.repository.type}") String repositoryType,
            @Value("${discodeit.repository.file.storage-path}") String storagePath
    ) {
        if ("jcf".equalsIgnoreCase(repositoryType)) {
            return new JCFBinaryContentRepository();
        }else {
            return new FileBinaryContentRepository(storagePath);
        }
    }
}
