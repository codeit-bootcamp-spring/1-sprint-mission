package com.sprint.mission.discodeit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker  // STOMP 프로토콜을 사용하는 메시지 브로커를 활성화
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final ChannelInterceptor jwtChannelInterceptor;
  private final ChannelInterceptor authorizationChannelInterceptor;
  private final ChannelInterceptor securityContextChannelInterceptor;

  // 메시지 브로커 관련 설정
  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {

    // 메시지를 받을 때 사용할 prefix
    // 클라이언트가 - /app/chat.sendMessage로 메시지를 보내면
    // 서버 측 @MessageMapping("/chat.sendMessage") 로 라우팅 됨
    registry.setApplicationDestinationPrefixes("/pub");

    // 클라이언트가 구독할 수 있는 prefix
    //
    registry.enableSimpleBroker("/sub");
  }

  // 클라이언트가 WebSocket 연결을 맺기 위한 엔드포인트 정의
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*")
        .withSockJS();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(
        jwtChannelInterceptor,
        securityContextChannelInterceptor, // 인증 정보 유지를 위함
        authorizationChannelInterceptor   // 인가 처리
    );
  }
}
