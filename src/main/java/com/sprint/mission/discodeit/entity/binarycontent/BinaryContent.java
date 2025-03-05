package com.sprint.mission.discodeit.entity.binarycontent;

import com.sprint.mission.discodeit.entity.ImmutableBaseObject;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinaryContent extends ImmutableBaseObject {

  private UUID fileId;
  private String binaryContentName;
  private Long size;
  private String contentType;
  private String bytes;
  private UploadBinaryContent uploadFile;
  private List<UploadBinaryContent> uploadFiles;
  private List<UUID> attachmentIds;

  public BinaryContent(UUID messageId, String uploadFileName, Long size,
      List<UploadBinaryContent> uploadFiles, Object o) {
  }

  public static BinaryContent createBinaryContent(UUID fileId, String name,
      Long size, String contentType, String bytes, UploadBinaryContent file,
      List<UploadBinaryContent> files) {

    return new BinaryContent(fileId, name, size, contentType, bytes, file, files, null);
  }

  public BinaryContent(UUID fileId, String binaryContentName, UploadBinaryContent uploadFile,
      List<UUID> attachmentIds) {
    this.fileId = fileId;
    this.binaryContentName = binaryContentName;
    this.uploadFile = uploadFile;
    this.attachmentIds = attachmentIds;
  }

  public BinaryContent(UUID uuid, String uploadFileName, UploadBinaryContent file) {
    this.fileId = uuid;
    this.binaryContentName = uploadFileName;
    this.uploadFile = file;
  }
}
