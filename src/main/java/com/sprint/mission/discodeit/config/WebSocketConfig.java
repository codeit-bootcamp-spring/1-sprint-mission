package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 웹소켓 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws") // 클라이언트가 웹소켓에 연결되기 위한 엔드 포인트
        .withSockJS(); // SockJS 연결, 웹소켓 지원하지 않는 브라우저에서도 지원할 수 있도록 설정
  }


  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // 메모리 기반 심플 브로커
    // 클라이언트는 해당 접두사로 시작하는 채널을 구독할 수 있다.
    config.enableSimpleBroker("/sub");
    // 해당 접두사로 시작하는 메세지는 @MessageMapping이 달린 메서드로 라우팅 된다
    // 클라이언트가 서버로 메세지를 보낼 때 해당 접두사를 사용한다.
    config.setApplicationDestinationPrefixes("/pub");
  }

}
