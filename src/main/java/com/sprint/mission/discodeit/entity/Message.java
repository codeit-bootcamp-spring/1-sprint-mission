package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class Message {

    private final UUID msguuId;
    // 소속된 채널?? 보낼 채널??, 목적지 채널.
    private final UUID destinationChuuId;
    private final Long createdAt;
    private Long updatedAt;
    //메시지 내용을 저장
    private String content;
    //송신 유저의 UUID
    private final UUID SendUseruuId;


    // 생성자
    public Message(User SendUserId, Channel destinationCh, String content) {
        this.msguuId = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.content = content;
        this.SendUseruuId = SendUserId.getuuID();
        this.destinationChuuId = destinationCh.getuuId();
    }

    public UUID getMsguuId() {
        return msguuId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }

    public UUID  SendUseruuId() {
        return SendUseruuId;
    }

    public UUID getDestinationChannel() {
        return destinationChuuId;
    }

    // update 함수
    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis(); // 수정 시간을 갱신
    }
    //흠 목적지채널을 저정할 변수에 UUID를 해야할 이유를 모르겠..어요.. 일단 했는데
    //객체로 저장하는건 별로인가... 막상 toString으로 뽑을려니.. UUID가 너무 길어서 특정도 쉽지않고 가독성이 떨어짐...니다.
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Message{\n" +
                "UUId=" + msguuId +
                ", \ndestinationChannel UUID = " + destinationChuuId +
                ", \ncreatedAt = " + sdf.format(new java.util.Date(createdAt)) +
                ", \nupdatedA t= " + sdf.format(new java.util.Date(updatedAt)) +
                ", \ncontent ='" + content + '\'' +
                ", \nsendUseruuId = " + SendUseruuId +
                "}";
    }

}
