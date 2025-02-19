package some_path._1sprintmission.discodeit.service;


import some_path._1sprintmission.discodeit.dto.ChannelDTO;
import some_path._1sprintmission.discodeit.dto.ChannelUpdateDTO;
import some_path._1sprintmission.discodeit.dto.PrivateChannelDTO;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.enums.ChannelType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    Channel createPrivateChannel(PrivateChannelDTO privateChanneldto);
    Channel createPublicChannel(String channelName);
    ChannelDTO find(UUID channelId);
    List<ChannelDTO> findAllByUserId(UUID userId);
    ChannelDTO update(ChannelUpdateDTO dto);
    void delete(UUID id);
    void joinChannelByInviteCode(UUID userId, String inviteCode);
}
