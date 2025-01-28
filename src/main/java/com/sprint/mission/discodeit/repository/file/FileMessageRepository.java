package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import java.util.HashMap;
import java.util.UUID;

public class FileMessageRepository {
    private static final HashMap<UUID, Message> messagesMap = new HashMap<UUID, Message>();

    // 외부에서 생성자 접근 불가
    private FileMessageRepository() {}

    // 레포지토리 객체 LazyHolder 싱글톤 구현.
    private static class FileMessageRepositoryHolder {
        private static final FileMessageRepository INSTANCE = new FileMessageRepository();
    }

    // 외부에서 호출 가능한 싱글톤 인스턴스.
    public static FileMessageRepository getInstance() {
        return FileMessageRepository.FileMessageRepositoryHolder.INSTANCE;
    }


}
