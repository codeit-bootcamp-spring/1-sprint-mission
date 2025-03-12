package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.ChannelDto;
import com.sprint.mission.entity.main.Channel;
import org.mapstruct.*;

import static org.mapstruct.MappingInheritanceStrategy.*;

@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
@Javadoc(
        """
        <h2>ChannelMapper</h2>
        채널 관련 DTO 변환을 위한 Mapper입니다.
        채널 조회 시 사용되는 DTO 변환을 담당합니다.
        """
)
public interface ChannelMapper {

    ChannelDto toDto(Channel channel);


}
