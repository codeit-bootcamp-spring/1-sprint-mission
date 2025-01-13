package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    // DB 대체 Channel data
    // Map에 키를 pk로 설정함으로써 수정, 삭제와 같이 검색 기능이 필요한 코드가 절대적으로 짧아짐
    // 검색/삽입 시 시간복잡도: Map - O(1), List - O(n)
    private final Map<UUID, Channel> data = new HashMap<>();
    
    // 클래스 필드로 작성 시 클래스 내부 메소드에서 모두 공유 가능
    private final UserService userService = JCFUserService.getInstance();


    // data 공유 위해 싱글톤 패턴으로 작성
    private static ChannelService channelService;

    private JCFChannelService() {}

    public static ChannelService getInstance() {
        if (channelService == null) {
            channelService = new JCFChannelService();
        }

        return channelService;
    }

    // 채널 생성
    @Override
    public void craete(UUID ownerId, String category, String name, String explanation) {

        UserService userService = JCFUserService.getInstance();

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

        data.put(channel.getId(), channel);

        System.out.println("[ 채널 생성 ]");
    }


    // 읽기
    // 채널 단건 조회
    @Override
    public Channel read(UUID id) {

        return data.get(id);

    }

    // 모든 유저의 모든 채널 반환
    @Override
    public List<Channel> allRead() {

        return data.values().stream().toList();
        
        // stream().toList()
        // JDK 16에서 추가된 메서드
        // Collectors.UnmodifiableList 또는 Collectors.UnmodifiableRandomAccessList 반환
        // -> 수정 불가한 구현체
        // 읽기 기능에서는 구현체 수정이 불필요하기 때문에 적합하다고 생각
        
        //stream().collect(Collectors.toList());
        // ArrayList 반환 -> 수정 가능한 구현체
    }

    // 수정
    // 카테고리 수정
    @Override
    public void updateCategory(UUID id, String updateCategory) {

        data.get(id).updateCategory(updateCategory);
    }

    // 채널 이름 수정
    @Override
    public void updateName(UUID id, String updateName) {

        data.get(id).updateName(updateName);
    }

    // 채널 설명 수정
    @Override
    public void updateExplanation(UUID id, String updateExplanation) {

        data.get(id).updateExplanation(updateExplanation);
    }

    // 멤버 수정
    // 멤버가 List에 있을 경우 멤버 삭제, 없을 경우 추가
    @Override
    public void updateMembers(UUID id, UUID memberId) {

        if (userService.read(memberId) == null) {
            System.out.println("없는 계정입니다.");
            return;
        }

        data.values().stream()
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

        data.remove(id);
    }
}