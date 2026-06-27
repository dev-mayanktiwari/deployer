package tech.mayanktiwari.deployer.project.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.mayanktiwari.deployer.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

  List<Project> findByUserId(UUID userId);

  Optional<Project> findByIdAndUserId(UUID projectId, UUID userId);

  boolean existsByNameAndUserId(String name, UUID userId);
}
