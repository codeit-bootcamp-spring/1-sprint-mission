package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        log.info("DiscodeitApplication start...");
        SpringApplication.run(DiscodeitApplication.class, args); }
}
