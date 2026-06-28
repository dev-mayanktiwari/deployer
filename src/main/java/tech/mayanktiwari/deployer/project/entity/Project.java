package tech.mayanktiwari.deployer.project.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;
import tech.mayanktiwari.deployer.users.entity.User;

@Getter
@Setter
@Entity
@Table(
    name = "projects",
    indexes = {@Index(name = "idx_project_user_id", columnList = "user_id")},
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uq_project_username",
          columnNames = {"user_id", "name"})
    })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  @NotBlank
  @Size(min = 3, max = 100)
  @Column(nullable = false, length = 100)
  String name;

  @NotBlank
  @Size(min = 3, max = 100)
  @Column(nullable = false, length = 100)
  String branch;

  @NotBlank
  @URL
  @Column(nullable = false)
  String repositoryUrl;

  @NotBlank
  @Column(nullable = false, length = 100)
  String buildCommand;

  @NotBlank
  @Size(min = 3, max = 100)
  @Column(nullable = false, length = 100)
  String outputPath;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  User user;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
  List<ProjectEnvVar> envVars;
}
