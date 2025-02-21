package com.sprint.mission.controller;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ErrorTest {

    @GetMapping
    public String error() {
        throw new CustomException(ErrorCode.CANNOT_REQUEST_LAST_READ_TIME);
    }
}
