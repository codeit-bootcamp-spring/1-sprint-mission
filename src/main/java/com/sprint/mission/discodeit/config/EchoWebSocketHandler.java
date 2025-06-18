package com.sprint.mission.discodeit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class EchoWebSocketHandler extends TextWebSocketHandler {

  // 클라이언트가 연결되었을 때
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("클라이언트 연결됨: {}", session.getId());

    try {
      session.sendMessage(new TextMessage("연결되었습니다."));
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  // 클라이언트가 메시지를 보냈을 때
  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    // 클라이언트로부터 받은 텍스트 메세지를 추출한다.
    String receivedMessage = message.getPayload();
    log.debug("받은 메세지: {}", receivedMessage);

    // 에코 응답 - 받은 메세지를 그대로 돌려보냄
    String echoMessage = "Echo: " + receivedMessage;
    session.sendMessage(new TextMessage(echoMessage)); // 클라이언트에게 전송
  }

  // 클라이언트가 연결을 끊었을 때
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("클라이언트 연결 해제됨: {}", session.getId());
  }

  //오류 handler
  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    log.warn("WebSocket 오류: sessionId = {},  exception: {}", session.getId(),
        exception.getMessage());
  }
}
