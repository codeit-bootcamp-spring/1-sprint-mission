package com.sprint.mission.discodeit.controller;

import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController("/api/sse")
public class SseController {

  // SSE 연결 엔드포인트
  // 클라이언트가 GET 요청으로 /api/sse 에 접속하면 SSE 연결이 시작됨
  @GetMapping
  public SseEmitter streamData() {
    // SSE 연결 생성 (타임아웃: 무제한) => 데이터를 지속적으로 보낼 수 있는 연결
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    try {
      //연결 즉시 환영 메세지 전송
      sseEmitter.send(SseEmitter.event()
          .name("welcome")   // 이벤트 이름 지정 -> 클라이언트가 필터링 가능
          .data("SSE 연결 성공")); // 전송할 내용
    } catch (IOException e) {
      sseEmitter.completeWithError(e); //메세지 전송 도중 오류 발생 시 연결 종료
    }

    // SseEmitter 반환 --> 클라이언트와 연결 유지됨
    return sseEmitter;
  }

}
