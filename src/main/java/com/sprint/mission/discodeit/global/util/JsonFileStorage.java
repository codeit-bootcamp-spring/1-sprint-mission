package com.sprint.mission.discodeit.global.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonFileStorage<T> implements FileStorage<T> {
	private final Class<T> type;
	private final ObjectMapper objectMapper;

	public JsonFileStorage(Class<T> type) {
		this.type = type;
		this.objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@Override
	public void init(Path directory) {
		try {
			if (!Files.exists(directory)) {
				Files.createDirectories(directory);
			} else if (!Files.isDirectory(directory)) {
				log.error("🚨 오류: 저장 경로가 파일로 존재함. 삭제 후 디렉토리 생성.");
				Files.delete(directory);
				Files.createDirectories(directory);
			}
		} catch (IOException e) {
			throw new RuntimeException("디렉토리 생성 실패: " + directory, e);
		}
	}

	@Override
	public void save(Path filePath, List<T> data) {
		try {
			if (Files.exists(filePath) && Files.isDirectory(filePath)) {
				log.error("🚨 오류: {}가 디렉토리로 잘못 생성됨. 삭제 후 재생성합니다.", filePath);
				Files.delete(filePath);
			}
			objectMapper.writeValue(filePath.toFile(), data);
			log.info("📌 파일 저장 완료: {}", data);
		} catch (IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}
	}

	@Override
	public List<T> load(Path filePath) {
		if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
			log.info("⚠ 파일이 존재하지 않거나, 디렉토리로 잘못 생성됨: {}", filePath);
			return new ArrayList<>();
		}
		try {
			JavaType type = objectMapper.getTypeFactory()
				.constructCollectionType(List.class, this.type);
			List<T> data = objectMapper.readValue(filePath.toFile(), type);
			log.info("✅ 파일 로드 성공: {}", data);
			return data;
		} catch (IOException e) {
			log.error("❌ 파일 로드 실패: {}", e.getMessage());
			return new ArrayList<>();
		}
	}
}