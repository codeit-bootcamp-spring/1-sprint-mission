package com.spirnt.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // STOMP 엔트포인트 설정 -> 클라이언트가 /ws로 접속해서 웹소켓 연결
            .setAllowedOriginPatterns("*") // CORS 정책 허용
            .withSockJS(); // SockJS 폴백 지원
    }

    // 메시지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트가 구독할 주소 prefix
        // 구독 엔드포인트: /sub/channels.{channelId}.messages
        config.enableSimpleBroker("/sub");

        // 클라이언트가 서버에 메시지를 보낼 때 사용하는 prefix
        // /pub/messages
        config.setApplicationDestinationPrefixes("/pub");
    }
}
