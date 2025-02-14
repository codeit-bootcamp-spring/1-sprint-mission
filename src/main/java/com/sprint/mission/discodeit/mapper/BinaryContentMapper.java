package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class BinaryContentMapper {
  public BinaryContent toProfilePicture(MultipartFile mFile, String userId) throws IOException {
    return new BinaryContent.BinaryContentBuilder(userId, mFile.getName(), mFile.getContentType(), mFile.getSize(), mFile.getBytes())
        .isProfilePicture()
        .build();
  }


}
