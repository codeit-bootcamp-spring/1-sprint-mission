package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.Identifiable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Channel extends BaseEntity implements Serializable, Identifiable {
  private static final long serialVersionUID = 1L;
  private final User owner;
  private String name;
  private List<User> members;
  
  public Channel(String name, User owner, List<User> members) {
    super();
    this.name = name;
    this.owner = owner;
    this.members = members;
  }
  
  public User getOwner() {
    return owner;
  }
  
  public String getName() {
    return name;
  }
  
  public List<Identifiable> getMembers() {
    return new ArrayList<>(members);
  }
  
  public void updateName(String name) {
    this.name = name;
    super.update();
  }
  
  public void updateMembers(List<User> members) {
    this.members = members;
    super.update();
  }
  
}
