package com.sprint.mission.discodeit.global.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SerializableFileStorage<T> implements FileStorage<T> {
	private final Class<T> type;

	public SerializableFileStorage(Class<T> type) {
		this.type = type;
	}

	@Override
	public void init(Path directory) {
		try {
			if (!Files.exists(directory)) {
				Files.createDirectories(directory); // 디렉토리가 없으면 생성
			} else if (!Files.isDirectory(directory)) {
				// 디렉토리 대신 파일이 존재하면 삭제 후 디렉토리 생성
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
			// 파일 경로가 디렉토리인지 확인하고 삭제
			if (Files.exists(filePath) && Files.isDirectory(filePath)) {
				log.error("🚨 오류: " + filePath + "가 디렉토리로 잘못 생성됨. 삭제 후 재생성합니다.");
				Files.delete(filePath);
			}

			// 데이터 저장
			try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
				oos.writeObject(data);
				oos.flush();
				log.info("📌 파일 저장 완료: " + data);
			}
		} catch (IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}
	}

	@Override
	public List<T> load(Path filePath) {
		if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
			log.info("⚠ 파일이 존재하지 않거나, 디렉토리로 잘못 생성됨: " + filePath);
			return new ArrayList<>();
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
			List<T> data = (List<T>)ois.readObject();
			log.info("✅ 파일 로드 성공: " + data);
			return data;
		} catch (IOException | ClassNotFoundException e) {
			log.error("❌ 파일 로드 실패: " + e.getMessage());
			return new ArrayList<>();
		}
	}
}