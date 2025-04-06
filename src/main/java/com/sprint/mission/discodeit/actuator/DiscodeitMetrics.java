package com.sprint.mission.discodeit.actuator;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscodeitMetrics {

    private final MeterRegistry registry;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        // 전체 메시지 수
        Gauge.builder("discodeit.messages.total", messageRepository::count)
            .description("Total number of messages")
            .register(registry);

        // 전체 채널 수
        Gauge.builder("discodeit.channels.total", channelRepository::count)
            .description("Total number of channels")
            .register(registry);

        // 전체 사용자 수
        Gauge.builder("discodeit.users.total", userRepository::count)
            .description("Total number of users")
            .register(registry);

        // 공개 채널 수
        Gauge.builder("discodeit.channels.public", () -> channelRepository.countByType("PUBLIC"))
            .description("Number of public channels")
            .register(registry);

        // 비공개 채널 수
        Gauge.builder("discodeit.channels.private", () -> channelRepository.countByType("PRIVATE"))
            .description("Number of private channels")
            .register(registry);
    }
} 