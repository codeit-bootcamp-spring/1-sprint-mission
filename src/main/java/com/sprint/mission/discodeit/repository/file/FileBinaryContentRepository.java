package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.AbstractFileRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.FileConstant.BINARY_CONTENT_FILE;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file")
public class FileBinaryContentRepository extends AbstractFileRepository<BinaryContent> implements BinaryContentRepository{

  public FileBinaryContentRepository(@Value("${app.file.binary-content-file}") String filePath) {
    super(filePath);
  }

  private List<BinaryContent> getFromFile(){
    return loadAll(BinaryContent.class);
  }

  private void writeToFile(List<BinaryContent> contents){
    saveAll(contents);
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
    return  Optional.ofNullable(getFromFile())
        .orElse(Collections.emptyList()).stream()
        .filter(content -> channelId.equals(content.getChannelId()))
        .toList();
  }

  @Override
  public List<BinaryContent> findByMessageId(String messageId) {
    return getFromFile().stream()
        .filter(content -> messageId.equals(content.getMessageId()))
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
    contents.removeIf(content -> messageId.equals(content.getMessageId()));
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
