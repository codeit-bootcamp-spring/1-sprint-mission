package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.FileConstant.BINARY_CONTENT_FILE;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

  private List<BinaryContent> getFromFile(){
    return FileUtil.loadAllFromFile(BINARY_CONTENT_FILE, BinaryContent.class);
  }

  private void writeToFile(List<BinaryContent> contents){
    FileUtil.saveAllToFile(BINARY_CONTENT_FILE, contents);
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {

    List<BinaryContent> contents = getFromFile();

    contents.removeIf(content -> content.getUUID().equals(binaryContent.getUUID()));
    contents.add(binaryContent);

    writeToFile(contents);
    return binaryContent;
  }

  @Override
  public List<BinaryContent> saveMultipleBinaryContent(List<BinaryContent> binaryContents) {

    Set<BinaryContent> contents = getFromFile().stream().collect(Collectors.toSet());

    for(BinaryContent content : binaryContents){
      if(contents.contains(content)) contents.remove(content);
    }

    binaryContents.stream().forEach(content -> contents.add(content));

    List<BinaryContent> contentList = contents.stream().toList();
    writeToFile(contentList);
    return contentList;
  }

  @Override
  public Optional<BinaryContent> findById(String id) {
    return getFromFile().stream().filter(content -> content.getUUID().equals(id)).findAny();
  }

  @Override
  public List<BinaryContent> findAll() {
    return getFromFile();
  }

  @Override
  public List<BinaryContent> findByUserId(String userId) {
    return getFromFile().stream()
        .filter(content -> content.getUserId()
            .equals(userId)).toList();
  }

  @Override
  public List<BinaryContent> findByChannel(String channelId) {
    return getFromFile().stream()
        .filter(content -> content.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public List<BinaryContent> findByMessageId(String messageId) {
    return getFromFile().stream()
        .filter(content -> content.getMessageId().equals(messageId))
        .toList();
  }

  @Override
  public void deleteByUserId(String userId) {
    List<BinaryContent> contents = getFromFile();
    contents.removeIf(content -> content.getUserId().equals(userId));
    writeToFile(contents);
  }

  @Override
  public void deleteByMessageId(String messageId) {
    List<BinaryContent> contents = getFromFile();
    contents.removeIf(content -> content.getMessageId().equals(messageId));
    writeToFile(contents);
  }

  @Override
  public void deleteById(String id) {
    List<BinaryContent> contents = getFromFile();
    contents.removeIf(content -> content.getUUID().equals(id));
    writeToFile(contents);
  }

  @Override
  public void clear() {

  }
}
