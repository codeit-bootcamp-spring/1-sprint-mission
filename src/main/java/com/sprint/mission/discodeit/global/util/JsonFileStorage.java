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
				Files.delete(filePath);
			}
			objectMapper.writeValue(filePath.toFile(), data);
		} catch (IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}
	}

	@Override
	public List<T> load(Path filePath) {
		if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
			return new ArrayList<>();
		}
		try {
			JavaType type = objectMapper.getTypeFactory()
				.constructCollectionType(List.class, this.type);
			List<T> data = objectMapper.readValue(filePath.toFile(), type);
			return data;
		} catch (IOException e) {
			return new ArrayList<>();
		}
	}
}