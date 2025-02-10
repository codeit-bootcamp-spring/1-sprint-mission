package com.sprint.mission.log;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TraceId {

    private String id;
    private int level;

    public TraceId() {
        this.id = UUID.randomUUID().toString().split("-")[0];
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    public TraceId createNextId(){
        return new TraceId(id, level+1);
    }

    public TraceId createPrevId(){
        return new TraceId(id, level-1);
    }



}
