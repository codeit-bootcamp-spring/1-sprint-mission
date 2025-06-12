package com.sprint.mission.discodeit.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

// 이 컴포넌트는 서비스 코드에서 publish() 호출만으로 이벤트를 발행할 수 있게 도와줌.
@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(NotificationEvent event) {
        publisher.publishEvent(event);
    }
}