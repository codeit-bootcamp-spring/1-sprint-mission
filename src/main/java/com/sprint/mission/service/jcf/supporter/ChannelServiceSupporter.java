package com.sprint.mission.service.jcf.supporter;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutBucketInventoryConfigurationRequest;

import java.util.List;

import static com.sprint.mission.common.exception.ErrorCode.*;
import static com.sprint.mission.entity.main.ChannelType.*;

@Component
@RequiredArgsConstructor
public class ChannelServiceSupporter {

    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    public ChannelDto ConvertPrivateChannelToDto(List<Channel> channels) {
        channels.forEach(channel -> {
            if (channel.getChannelType().equals(PUBLIC)) throw new CustomException(CANNOT_CONVERT_TO_DTO);
        });



        return null;
    }
}
