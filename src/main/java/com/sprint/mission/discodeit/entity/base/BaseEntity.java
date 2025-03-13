package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
//@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseEntity {
    @Id @GeneratedValue
    protected UUID id;
    @CreatedDate @NotNull
    protected Instant createdAt;

}
