package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  UUID id;
  @CreatedDate
  @Column(columnDefinition = "timestamp with time zone", updatable = false, nullable = false)
  Instant createAt;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recive_id", columnDefinition = "uuid")
  User receiveUser;
  @Column(length = 50, nullable = false, unique = true)
  String title;
  @Enumerated(EnumType.STRING)
  NotificatonType type;
  @Column(columnDefinition = "target", nullable = false)
  UUID targetId;


  public Notification(User user, String title,NotificatonType type, UUID targetId){
    this.id=UUID.randomUUID();
    this.createAt= Instant.now();
    this.receiveUser=user;
    this.title=title;
    this.type=type;
    this.targetId=targetId;
  }

}
