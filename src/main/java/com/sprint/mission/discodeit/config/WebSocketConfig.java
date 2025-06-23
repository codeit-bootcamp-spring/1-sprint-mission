package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.DiscodeitChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.security.messaging.context.SecurityContextPropagationChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final DiscodeitChannelInterceptor channelInterceptor;
  private final ChannelInterceptor authorizationChannelInterceptor;

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(
        channelInterceptor,
        new SecurityContextChannelInterceptor(),
        new SecurityContextPropagationChannelInterceptor()
//        authorizationChannelInterceptor
    );
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/sub");
    registry.setApplicationDestinationPrefixes("/pub");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
        .addEndpoint("/ws")
        .setAllowedOriginPatterns("*")
        .withSockJS();
  }
}
