package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.file.FileServiceFactory;
import com.sprint.mission.discodeit.factory.jcf.JCFServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileNotFoundException;

public class FactoryProvider {
    private final ServiceFactory serviceFactory;

    // private 생성자: 실행 시점에 모드 선택
    private FactoryProvider(String mode) {
        if ("JCF".equalsIgnoreCase(mode)) {
            this.serviceFactory = new JCFServiceFactory();
        } else if ("FILE".equalsIgnoreCase(mode)) {
            this.serviceFactory = new FileServiceFactory();
        } else if ("BASIC".equalsIgnoreCase(mode)) {
            this.serviceFactory = new BasicServiceFactory();
        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }

    // 싱글톤의 thread safe를 위해 DCL 방식을 생각하다 static initializer block으로 구현
    private static class Holder {
        private static FactoryProvider instance;

        private static void initialize(String mode) {
            if (instance == null) {
                instance = new FactoryProvider(mode);
            }
        }

        private static FactoryProvider getInstance() {
            if (instance == null) {
                throw new IllegalStateException("FactoryProvider is not initialized. Call initialize() first.");
            }
            return instance;
        }
    }

    public static void initialize(String mode) {
        Holder.initialize(mode);
    }

    public static FactoryProvider getInstance() {
        return Holder.getInstance();
    }

    // ServiceFactory를 반환
    public ServiceFactory getServiceFactory() {
        return serviceFactory;
    }
}
