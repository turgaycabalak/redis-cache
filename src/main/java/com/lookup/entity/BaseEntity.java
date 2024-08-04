package com.lookup.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "last_updated", nullable = false, columnDefinition = "timestamp default current_timestamp")
  private LocalDateTime lastUpdated;

  @Column(name = "created_date", nullable = false, columnDefinition = "timestamp default current_timestamp")
  private LocalDateTime createdDate;

  @Column(nullable = false, columnDefinition = "boolean default true")
  private Boolean active;

  @PrePersist
  public void prePersist() {
    if (createdDate == null) {
      this.createdDate = LocalDateTime.now();
    }
    if (lastUpdated == null) {
      this.lastUpdated = LocalDateTime.now();
    }
    if (active == null) {
      this.active = true;
    }
  }

  @PreUpdate
  public void preUpdate() {
    this.lastUpdated = LocalDateTime.now();
  }
}
