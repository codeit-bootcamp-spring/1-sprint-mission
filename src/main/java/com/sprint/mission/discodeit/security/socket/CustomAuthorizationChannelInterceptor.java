package com.sprint.mission.discodeit.security.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor;


@Slf4j
public class CustomAuthorizationChannelInterceptor implements ChannelInterceptor {

  private final AuthorizationChannelInterceptor delegate;

  public CustomAuthorizationChannelInterceptor(
      AuthorizationManager<Message<?>> authorizationManager) {
    this.delegate = new AuthorizationChannelInterceptor(authorizationManager);
  }

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    log.info("🚀 인가 처리 시작: {}", message.getHeaders());
    return delegate.preSend(message, channel);
  }
}
