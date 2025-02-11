package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {
    private final UUID id;
    private final Instant createdAt;

    private char[] img;

    private BinaryContent() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.img=null;
    }

    private BinaryContent( char [] img) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.img=img;
    }

    private BinaryContent(UUID id, Instant createdAt, char[] img) {
        this.id = id;
        this.createdAt = createdAt;
        this.img=img;
    }

    public static BinaryContent setUpBinaryContent( ){
        return new BinaryContent();
    }

    public static BinaryContent setUpBinaryContent( char[] img){
        return new BinaryContent(img);
    }

    public static BinaryContent makeAllBinaryContent(UUID id, Instant createdAt  , char[] img) {
        if (img == null) {
            img = new char[0];  // 기본 빈 값 설정
        }
        return new BinaryContent(id, createdAt, img);
    }
    public BinaryContent makeNewBinaryContent(char[] img){

        return new BinaryContent(img);
    }
}
