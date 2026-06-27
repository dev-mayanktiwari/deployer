package tech.mayanktiwari.deployer.project.mapper;

import org.springframework.stereotype.Component;
import tech.mayanktiwari.deployer.project.dto.CreateProjectDTO;
import tech.mayanktiwari.deployer.project.dto.ProjectResponseDTO;
import tech.mayanktiwari.deployer.project.entity.Project;
import tech.mayanktiwari.deployer.users.entity.User;

@Component
public class ProjectMapper {

  public Project toEntity(CreateProjectDTO createProjectDTO, User user) {
    Project project = new Project();
    project.setName(createProjectDTO.getName());
    project.setRepositoryUrl(createProjectDTO.getRepositoryUrl());
    project.setBranch(createProjectDTO.getBranch());
    project.setBuildCommand(createProjectDTO.getBuildCommand());
    project.setOutputPath(createProjectDTO.getOutputPath());
    project.setUser(user);
    return project;
  }

  public ProjectResponseDTO toResponse(Project project) {
    return new ProjectResponseDTO(
        project.getId(),
        project.getName(),
        project.getRepositoryUrl(),
        project.getBranch(),
        project.getBuildCommand(),
        project.getOutputPath(),
        project.getCreatedAt());
  }
}
