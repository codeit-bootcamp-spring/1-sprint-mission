package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.util.FileUtil;

import java.util.List;

/**
 * 이 추상 클래스는 파일을 데이터 저장소로 사용하는 기본 저장소 기능을 제공
 * 특정 엔티티 타입 T에 대한 데이터를 파일에서 로드하고 저장하는 기능을 정의
 *
 * @param <T> 저장할 엔티티 타입
 */
public abstract class AbstractFileRepository<T> {
  private final String filePath;
  protected String getFilePath() {
    return filePath;
  }
  protected AbstractFileRepository(String filePath) {
    this.filePath = filePath;
  }

  protected List<T> loadAll(Class<T> clazz){
    return FileUtil.loadAllFromFile(filePath, clazz);
  }
  protected void saveAll(List<T> data){
    FileUtil.saveAllToFile(filePath, data);
  }


}
