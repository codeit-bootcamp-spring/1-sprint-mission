package com.sprint.mission.controller;

import com.sprint.mission.service.exception.DuplicateName;
import com.sprint.mission.service.exception.NotFoundId;
import jakarta.servlet.http.PushBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    // 나중에 인터페이스 완료하면 ㄱ

    @GetMapping("/api/create")
    public void create() {
        if (3 > 0){
            throw new DuplicateName();
        }
    }

    @GetMapping
    public void findById(){

    }

//    @ExceptionHandler(value = NotFoundId.class)
//    public ResponseEntity<Map<String, String>> exceptionHandler(NotFoundId e){
//
//        log.info("++++++++++++++++++++++++++++++++++++++++ㅅㅂ 왜 안돼");
//        HttpStatus status = HttpStatus.NOT_FOUND;
//
//        Map<String, String> map = new HashMap<>();
//        map.put("code", "404");
//        map.put("error type",status.getReasonPhrase());
//        map.put("message", "찾을 수 없는 리소스입니다");
//
//        return new ResponseEntity<>(map, status);
//    }

}
