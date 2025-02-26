package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.AbstractFileRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file",  matchIfMissing = true)
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

    contents.removeIf(content -> content.getId().equals(binaryContent.getId()));
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

    contents.addAll(binaryContents);

    List<BinaryContent> contentList = contents.stream().toList();
    writeToFile(contentList);
    return contentList;
  }

  @Override
  public Optional<BinaryContent> findById(String id) {
    return getFromFile().stream().filter(content -> content.getId().equals(id)).findAny();
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
  public List<BinaryContent> findProfilesOf(Set<String> users) {
    List<BinaryContent> contents = getFromFile();

    return contents.stream()
        .filter(content -> content.isProfilePicture() && users.contains(content.getUserId()))
        .toList();
  }

  @Override
  public BinaryContent findByUserIdAndIsProfilePictureTrue(String userId) {
    List<BinaryContent> contents = getFromFile();

    return contents.stream()
        .filter(content -> content.isProfilePicture() && content.getUserId().equals(userId))
        .findFirst()
        .orElse(null);
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
    contents.removeIf(content -> content.getId().equals(id));
    writeToFile(contents);
  }

  @Override
  public void clear() {
    File file = new File(getFilePath());
    if(file.exists()) file.delete();
  }

  @PostConstruct
  private void postContruct(){
    clear();
  }
}
