package com.lookup.entity;

import jakarta.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@SQLRestriction("active = true")
public class Country extends BaseEntity {
  private String name;
  private String capital;
}
