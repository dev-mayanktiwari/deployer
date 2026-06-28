package tech.mayanktiwari.deployer.project.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.mayanktiwari.deployer.project.entity.ProjectEnvVar;

public interface ProjectEnvVarRepository extends JpaRepository<ProjectEnvVar, Integer> {

  List<ProjectEnvVar> findByProjectId(UUID projectId);

  Optional<ProjectEnvVar> findByProjectIdAndKey(UUID projectId, String key);
}
