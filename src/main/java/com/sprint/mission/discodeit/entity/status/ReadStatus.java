package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    //요구사항에서
    //사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다.
    // 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
    // ---> 채널별로 사용자의 읽음여부를 확인?? 할때 사용자가 속한 채널에대해서 확인을 해야함... 어떻게???
    //
    @Serial
    private static final long serialVersionUID = 1L;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastReadAt;
    private final UUID uuID;
    private final User user;
    private final Channel channel;

    public ReadStatus(User user, Channel channel){
        this.user = user;
        this.channel = channel;
        this.uuID = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void updateLastReadAt() {
        this.lastReadAt = Instant.now();
        this.updatedAt = lastReadAt;
    }



}
