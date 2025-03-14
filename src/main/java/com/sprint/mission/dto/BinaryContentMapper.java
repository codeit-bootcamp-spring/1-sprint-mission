package com.sprint.mission.dto;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.mappedDto.BinaryContentDto;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.MappingInheritanceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.mapstruct.MappingInheritanceStrategy.*;


//@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface BinaryContentMapper {
    BinaryContentDto toDto(BinaryContent binaryContent);

    default Optional<BinaryContentDtoForCreate> convertFileToBinaryContentDto(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Optional.empty();
        }
        try {
            BinaryContentDtoForCreate binaryContentDtoForCreate = new BinaryContentDtoForCreate(file.getName(),
                    file.getContentType(), file.getSize(), file.getBytes());
            return Optional.of(binaryContentDtoForCreate);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
        }
    }

    BinaryContent toEntity(BinaryContentDtoForCreate request);
}
