package com.sprint.mission.service.jcf.main;


import com.sprint.mission.entity.Channel;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.exception.DuplicateName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {

    private final JCFChannelRepository channelRepository;
    private final JCFUserService userService;

    //@Override
    public Channel create(Channel channel) {
        return channelRepository.save(channel);
    }

    //@Override
    public Channel update(Channel updatingChannel, String newName) {
        updatingChannel.setName(newName);
        return create(updatingChannel);
    }

    //@Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id);
    }

    //@Override
    public Set<Channel> findAll() {
        return channelRepository.findAll();
    }

    //@Override
    public void delete(Channel channel) {
        //channel.getUsersImmutable().stream().forEach(user -> user.removeChannel(channel));
        //deletingChannel.removeAllUser();
        channelRepository.delete(channel);
    }

    /**
     * 중복 검증
     */
    //@Override
    public void validateDuplicateName(String name) {
        boolean isDuplicate = findAll().stream()
                .anyMatch(channel -> channel.getName().equals(name));
        if (isDuplicate) {
            throw new DuplicateName(
                    String.format("%s는 이미 존재하는 이름의 채널명입니다", name));
        }
    }
}

