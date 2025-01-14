package com.sprint.mission.discodeit.app;

import com.sprint.mission.discodeit.entity.Data;
import com.sprint.mission.discodeit.jcf.JCFCreateService;
import com.sprint.mission.discodeit.jcf.JCFDeleteService;
import com.sprint.mission.discodeit.jcf.JCFSearchService;
import com.sprint.mission.discodeit.jcf.JCFUpdateService;
import com.sprint.mission.discodeit.service.CreateService;
import com.sprint.mission.discodeit.service.DeleteService;
import com.sprint.mission.discodeit.service.SearchService;
import com.sprint.mission.discodeit.service.UpdateService;

import java.util.UUID;




public class JavaApplication {
    public static void main(String[]arg){
        Data data = new Data();



        // Service 객체 생성
        CreateService createService = new JCFCreateService(data);
        SearchService searchService = new JCFSearchService(data);
        UpdateService updateService = new JCFUpdateService(data);
        DeleteService deleteService = new JCFDeleteService(data);

        // 샘플 UUID 생성
        UUID userUuid1 = UUID.randomUUID();
        UUID userUuid2 = UUID.randomUUID();

        // CREATE 작업
        createService.createUser(userUuid1, "user1", "password123");
        createService.createUser(userUuid2, "user2", "password456");
        createService.createMessage(userUuid1, "user1", "Hello, world!");
        createService.createChannel(userUuid2, "Tech Talk", "user2");

        // SEARCH 작업(수정 전)
        searchService.searchUserByUUID(userUuid1);
        searchService.searchChannelByUUID(userUuid2);
        searchService.searchMessageByUUID(userUuid1);
        searchService.searchAllChannels();
        searchService.searchAllUsers();

        // UPDATE 작업(수정)
        updateService.updateUser(userUuid1, "updatedUser1", "newPassword123");
        updateService.updateMessage(userUuid1, "Updated message content");
        updateService.updateChannel(userUuid2, "Updated Tech Talk");

        // 수정 후 출력
        searchService.searchUserByUUID(userUuid1);
        searchService.searchChannelByUUID(userUuid2);
        searchService.searchMessageByUUID(userUuid1);
        searchService.searchAllChannels();
        searchService.searchAllUsers();

        //DELETE 작업
        deleteService.deleteUser(userUuid2);
        deleteService.deleteChannel(userUuid2);

//        // 최종 상태 확인
        searchService.searchAllUsers();
        searchService.searchAllChannels();

    }





}
