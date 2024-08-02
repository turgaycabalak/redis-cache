package com.lookup.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "last_updated", nullable = false, columnDefinition = "timestamp default current_timestamp")
  private LocalDateTime lastUpdated;

  @Column(name = "created_date", nullable = false, columnDefinition = "timestamp default current_timestamp")
  private LocalDateTime createdDate;

  private boolean active;
}
