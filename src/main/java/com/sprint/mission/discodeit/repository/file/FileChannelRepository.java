package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final List<Channel> data;
    public FileChannelRepository(SerializationUtil<Channel> util){
        this.data = util.loadData();
    }

    @Override
    public boolean save(Channel channel){
        try {
            if (channel == null) {
                throw new IllegalArgumentException("채널 null일 수 없습니다.");
            }
            return data.add(channel); // 성공하면 true 반환
        } catch(IllegalArgumentException e) {
            System.out.println("채널 생성 실패: " + e.getMessage());
        } catch(Exception e) {
            System.out.println("알 수 없는 오류가 발생했습니다."); // 일반적인 예외 처리
        }
        return false; // 실패시 false 반환
    }

    @Override
    public Optional<Channel> findById(UUID id){
        if (id == null) {
            return Optional.empty();  // id가 null이면 빈 Optional을 반환
        }
        for (Channel ch : data){
            if(ch.getId().equals(id)){
                return Optional.of(ch);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Channel> findAll(){ return data; }

    @Override
    public boolean updateOne(UUID id, String name, String topic) {
        if (id == null) {
            System.out.println("채널 수정 실패");
            return false;
        }
        for (Channel ch : data) {
            if (ch.getId().equals(id)) {
                ch.update(name, topic); // data는 상수이므로 data.update(name, topic) 하면 안됨.
                return true;
            }
        }
        System.out.println("채널 수정 실패");
        return false;
    }

    @Override
    public boolean deleteOne(UUID id){
        if (id == null) {
            System.out.println("채널 삭제 실패");
            return false;
        }
        for (Iterator<Channel> itr = data.iterator(); itr.hasNext();) {
            Channel ch = itr.next();
            if (ch.getId().equals(id)) {
                itr.remove();
                return true;
            }
        }
        System.out.println("채널 삭제 실패");
        return false;
    }
}
