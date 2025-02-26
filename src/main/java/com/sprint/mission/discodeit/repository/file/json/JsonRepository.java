package com.sprint.mission.discodeit.repository.file.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.repository.RepositoryProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class JsonRepository<K, V> {

  private final ObjectMapper objectMapper;
  private final RepositoryProperties properties;
  private final String fileName;
  private final TypeReference<HashMap<K, V>> typeReference;
  protected Map<K, V> map = new HashMap<>();

  @PostConstruct
  protected void init() {
    createDirectory();
    loadFromJson();
  }

  protected V save(K key, V value) {
    map.put(key, value);
    saveToJson();
    return value;
  }

  protected V findById(K key) {
    return map.get(key);
  }

  protected Map<K, V> findAll() {
    return map;
  }

  protected void removeById(K key) {
    map.remove(key);
    saveToJson();
  }

  protected void saveToJson() {
    try {
      objectMapper.writeValue(new File(getFilePath()), map);
    } catch (IOException e) {
      throw new RuntimeException("Json 저장에 실패했습니다: " + fileName, e);
    }
  }

  protected void loadFromJson() {
    File file = new File(getFilePath());
    if (file.exists()) {
      try {
        map = objectMapper.readValue(file, typeReference);
      } catch (IOException e) {
        return;
      }
    } else {
      map = new HashMap<>();
    }
  }

  private void createDirectory() {
    File directory = new File(properties.getFileDirectory());
    if (!directory.exists()) {
      boolean isCreatedDirectory = directory.mkdirs();
      if (!isCreatedDirectory) {
        throw new RuntimeException("디렉토리 생성 오류: " + properties.getFileDirectory());
      }
    }
  }

  private String getFilePath() {
    return new File(properties.getFileDirectory(), fileName).getAbsolutePath();
  }


  // 데이터 초기화 메서드 추가
  public void clearData() {
    this.map.clear();
  }

  // 또는 data 필드를 직접 새로 할당
  public void resetData() {
    this.map = new HashMap<>();
  }
}
