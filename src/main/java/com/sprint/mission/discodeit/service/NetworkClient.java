package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.exception.ConnectException;
import com.sprint.mission.discodeit.exception.SendException;

public class NetworkClient {
    private final String address="https://discodeit.com";;//디스코드 앱에도 주소가 있을까요..?ㅎㅎㅎ
    public boolean connectError;
    public boolean sendError;

    public void connect() {
        if (connectError) {
            throw new ConnectException(address, address + " 서버 연결 실패");
        }
        System.out.println(address+" 서버 연결 성공");
    }
    public void send(String message) {
        if(sendError) {
            throw new SendException(message, address + " 서버에 메세지 전송 실패: " + message);
        }
    }
    public void disconnect() {
        System.out.println(address+" 서버 연결 해제");
    }

    public void initError(String connectAddress) {
        if (!connectAddress.equals(address)) {
            connectError=true;
        }
    }
}
