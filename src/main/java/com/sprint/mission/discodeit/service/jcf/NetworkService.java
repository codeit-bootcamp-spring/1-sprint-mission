package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.service.NetworkClient;

public class NetworkService extends NetworkClient {
    NetworkClient client=new NetworkClient();
    public void connect(String connectAddress) {
        client.initError(connectAddress);
        /*try{
            client.connect();
        }finally{
            client.disconnect();
        }*/
    }
    public void disconnect() {
        client.disconnect();
    }

}
