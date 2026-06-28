package tech.mayanktiwari.deployer.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(
    name = "project_env_var",
    indexes = {@Index(name = "idx_project_env_var_project_id", columnList = "project_id")},
    uniqueConstraints = {@UniqueConstraint(columnNames = {"project_id", "key"})})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectEnvVar {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @Column(nullable = false)
  String key;

  @Column(nullable = false, columnDefinition = "TEXT")
  String encryptedValue;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
  Project project;
}
