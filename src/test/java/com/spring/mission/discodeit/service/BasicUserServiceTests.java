package com.spring.mission.discodeit.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class BasicUserServiceTests{

    @BeforeEach
    void beforeEach(){
        //setup
        System.out.println("## BeforeEach");
    }

    @AfterEach
    void afterEach(){
        System.out.println("## AfterEach");
    }

    @Test
    void createUser(){

        //given: Mock 객체가 특정 상황에서 해야하는 행위
        //andExpect: 기대하는 값이 나왔는지 체크
        //andExpect.. andDo

        //verity: 해당 객체의 메소드가 실행되었는지 체크

    }
}