package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    // DB 대체 Channel data
    // Map<유저 이메일, 해당 유저 채널 목록>
    private final List<Channel> data = new ArrayList<>();

    // data 공유 위해 싱글톤 패턴으로 작성
    private static ChannelService channelService;

    private JCFChannelService() {
    }

    public static ChannelService getJCFChannelService() {
        if (channelService == null) {
            channelService = new JCFChannelService();
        }

        return channelService;
    }

    // 채널 생성
    @Override
    public void craete(UUID ownerId, String category, String name, String explanation) {

        UserService userService = JCFUserService.getJCFUserService();

        if (userService.read(ownerId) == null) {
            System.out.println("가입되지 않은 계정입니다.");
            return;
        }

        // 카테고리를 작성하지 않거나 스페이스만 입력할 경우
        if (category.isBlank()) {
            System.out.println("카테고리를 입력해주세요.");
            return;
        }
        if (name.isBlank()) {
            System.out.println("이름을 입력해주세요.");
            return;
        }

        Channel channel = new Channel(ownerId, category.trim(), name.trim(), explanation.trim());

        data.add(channel);

        System.out.println("[ 채널 생성 ]");
    }


    // 읽기
    // 채널 단건 조회
    @Override
    public Channel read(UUID id) {
        return data.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()  // null 처리때문에 Optional<> 타입 반환
                .orElse(null);

    }

    // 모든 유저의 모든 채널 반환
    @Override
    public List<Channel> allRead() {
        return data;
    }

    // 수정
    // 카테고리 수정
    @Override
    public void updateCategory(UUID id, String category) {
        data.stream()
                .filter(channel -> channel.getId().equals(id))
                .forEach(channel -> channel.updateCategory(category));
    }

    // 채널 이름 수정
    @Override
    public void updateName(UUID id, String name) {
        data.stream()
                .filter(channel -> channel.getId().equals(id))
                .forEach(channel -> channel.updateName(name));
    }

    // 채널 설명 수정
    @Override
    public void updateExplanation(UUID id, String explanation) {
        data.stream()
                .filter(channel -> channel.getId().equals(id))
                .forEach(channel -> channel.updateExplanation(explanation));
    }

    // 멤버 수정
    // 멤버가 List에 있을 경우 멤버 삭제, 없을 경우 추가
    @Override
    public void updateMembers(UUID id, UUID memberId) {
        UserService userService = JCFUserService.getJCFUserService();

        if (userService.read(memberId) == null) {
            System.out.println("없는 계정입니다.");
            return;
        }

        data.stream()
                .filter(channel -> channel.getId().equals(id))
                .forEach(channel -> {
                    if (channel.getMembers().contains(memberId)) {
                        channel.getMembers().remove(memberId);
                        channel.updateMembers(channel.getMembers());
                    } else {
                        channel.getMembers().add(memberId);
                    }
                });
    }

    // 채널 삭제
    @Override
    public void delete(UUID id) {
        Channel delChannel = data.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElse(null);

        data.remove(delChannel);
    }
}
