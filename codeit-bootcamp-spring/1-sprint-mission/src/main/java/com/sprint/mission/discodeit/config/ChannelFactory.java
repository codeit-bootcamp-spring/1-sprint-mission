package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.application.service.channel.JCFChannelService;
import com.sprint.mission.discodeit.application.service.interfaces.ChannelService;
import com.sprint.mission.discodeit.repository.channel.InMemoryChannelRepository;
import com.sprint.mission.discodeit.repository.channel.interfaces.ChannelRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ChannelFactory {

    private static final ChannelRepository CHANNEL_REPOSITORY = createChannelRepository();
    private static final ChannelService CHANNEL_SERVICE = new JCFChannelService(CHANNEL_REPOSITORY);

    public static ChannelService getChannelService() {
        return CHANNEL_SERVICE;
    }

    public static ChannelRepository createChannelRepository() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("application.yaml"));
        } catch (IOException exception) {
            System.out.println("설정 파일 로드 실패, 기본값(memory) 사용");
        }
        String repositoryType = props.getProperty("repository.type", "memory");

        return switch (repositoryType) {
            default -> new InMemoryChannelRepository();
        };
    }
}
