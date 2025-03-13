package com.sprint.mission.mapper;

import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.service.UserService;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Test {

    @Autowired
    private UserService userService;
    @Autowired
    private BinaryContentMapper binaryContentMapper;



}
