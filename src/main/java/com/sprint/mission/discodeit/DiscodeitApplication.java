package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class DiscodeitApplication {
  /* 만약 디렉토리가 없다면, 파일 저장 시 오류가 발생하기 때문에 프로그램이 시작될 때 자동으로 디렉토리를 생성하는 초기화 로직을 넣어야함.
    1. 클래스 안에 초기화 메서드
    2. psvm 안에 1) 디렉토리 선언 2) 초기화
   */

	public static void init(Path directory) {
		// 저장할 경로의 파일 초기화 메서드
		if (!Files.exists(directory)) {
			try {
				Files.createDirectories(directory);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);
	}

}
