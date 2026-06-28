package tech.mayanktiwari.deployer.project.mapper;

import org.springframework.stereotype.Component;
import tech.mayanktiwari.deployer.project.dto.ProjectRequestDTO;
import tech.mayanktiwari.deployer.project.dto.ProjectResponseDTO;
import tech.mayanktiwari.deployer.project.entity.Project;
import tech.mayanktiwari.deployer.users.entity.User;

@Component
public class ProjectMapper {

  public Project toEntity(ProjectRequestDTO projectRequestDTO, User user) {
    Project project = new Project();
    project.setName(projectRequestDTO.getName());
    project.setRepositoryUrl(projectRequestDTO.getRepositoryUrl());
    project.setBranch(projectRequestDTO.getBranch());
    project.setBuildCommand(projectRequestDTO.getBuildCommand());
    project.setOutputPath(projectRequestDTO.getOutputPath());
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
