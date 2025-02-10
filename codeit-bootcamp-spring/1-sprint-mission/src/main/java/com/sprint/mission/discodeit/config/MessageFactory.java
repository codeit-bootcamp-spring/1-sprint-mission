package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.application.service.interfaces.MessageService;
import com.sprint.mission.discodeit.application.service.message.JCFMessageService;
import com.sprint.mission.discodeit.repository.message.interfaces.MessageRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MessageFactory {

    private static final MessageRepository MESSAGE_REPOSITORY = createMessageRepository();
    private static final MessageService MESSAGE_SERVICE =
            new JCFMessageService(MESSAGE_REPOSITORY, ChannelFactory.getChannelService(), UserFactory.getUserService());

    public static MessageService getMessageService() {
        return MESSAGE_SERVICE;
    }

    public static MessageRepository createMessageRepository() {
        Properties properties = new Properties();
        try (InputStream is = ChannelFactory.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(is);
        } catch (IOException e) {
            System.out.println("설정 파일 로드 실패, 기본값(memory) 사용");
        }
        String repositoryType = properties.getProperty("repository.type", "memory");

        return switch (repositoryType) {
            default -> MESSAGE_REPOSITORY;
        };
    }
}
