package tech.mayanktiwari.deployer.project.service;

import static tech.mayanktiwari.deployer.common.config.Constants.PROJECT;
import static tech.mayanktiwari.deployer.common.config.Constants.USER;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.mayanktiwari.deployer.common.exception.ErrorCode;
import tech.mayanktiwari.deployer.project.dto.ProjectRequestDTO;
import tech.mayanktiwari.deployer.project.dto.ProjectResponseDTO;
import tech.mayanktiwari.deployer.project.entity.Project;
import tech.mayanktiwari.deployer.project.mapper.ProjectMapper;
import tech.mayanktiwari.deployer.project.repository.ProjectRepository;
import tech.mayanktiwari.deployer.users.entity.User;
import tech.mayanktiwari.deployer.users.service.UserService;

@Service
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final UserService userService;
  private final ProjectMapper projectMapper;

  public Optional<Project> findById(UUID id) {
    return projectRepository.findById(id);
  }

  public ProjectResponseDTO createProject(ProjectRequestDTO projectRequestDTO, UUID userId) {
    if (projectRepository.existsByNameAndUserId(projectRequestDTO.getName(), userId)) {
      throw ErrorCode.RESOURCE_ALREADY_EXISTS.build(PROJECT, projectRequestDTO.getName());
    }

    User user =
        userService
            .findById(userId)
            .orElseThrow(() -> ErrorCode.RESOURCE_NOT_FOUND.build(USER, userId));
    Project project = projectMapper.toEntity(projectRequestDTO, user);

    Project savedProject = projectRepository.save(project);

    return projectMapper.toResponse(savedProject);
  }

  public ProjectResponseDTO getProject(UUID projectId, UUID userId) {
    Project project =
        projectRepository
            .findByIdAndUserId(projectId, userId)
            .orElseThrow(() -> ErrorCode.RESOURCE_NOT_FOUND.build(PROJECT, projectId));

    return projectMapper.toResponse(project);
  }

  public List<ProjectResponseDTO> getProjects(UUID userId) {
    List<Project> projects = projectRepository.findByUserId(userId);

    return projects.stream().map(projectMapper::toResponse).toList();
  }

  public void deleteProject(UUID projectId, UUID userId) {
    Project project =
        projectRepository
            .findByIdAndUserId(projectId, userId)
            .orElseThrow(() -> ErrorCode.RESOURCE_NOT_FOUND.build(PROJECT, projectId));

    projectRepository.delete(project);
  }
}
