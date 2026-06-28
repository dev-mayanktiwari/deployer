package tech.mayanktiwari.deployer.project.service;

import static tech.mayanktiwari.deployer.common.config.Constants.ENV_VAR;
import static tech.mayanktiwari.deployer.common.config.Constants.PROJECT;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.mayanktiwari.deployer.common.exception.ErrorCode;
import tech.mayanktiwari.deployer.encryption.service.EncryptionService;
import tech.mayanktiwari.deployer.project.dto.ProjectEnvVarDTO;
import tech.mayanktiwari.deployer.project.entity.Project;
import tech.mayanktiwari.deployer.project.entity.ProjectEnvVar;
import tech.mayanktiwari.deployer.project.mapper.ProjectEnvVarMapper;
import tech.mayanktiwari.deployer.project.repository.ProjectEnvVarRepository;

@Service
@RequiredArgsConstructor
public class ProjectEnvVarService {

  private final EncryptionService encryptionService;
  private final ProjectService projectService;
  private final ProjectEnvVarRepository projectEnvVarRepository;
  private final ProjectEnvVarMapper projectEnvVarMapper;

  public List<ProjectEnvVarDTO> getEnvVars(UUID projectId, UUID userId) {
    Project project = resolveAndVerifyOwnership(projectId, userId);

    return projectEnvVarRepository.findByProjectId(project.getId()).stream()
        .map(
            envVar ->
                projectEnvVarMapper.toResponse(
                    envVar.getKey(), encryptionService.decrypt(envVar.getEncryptedValue())))
        .toList();
  }

  @Transactional
  public List<ProjectEnvVarDTO> upsertEnvVars(
      UUID projectId, UUID userId, List<ProjectEnvVarDTO> dtos) {
    Project project = resolveAndVerifyOwnership(projectId, userId);

    for (ProjectEnvVarDTO dto : dtos) {
      String encrypted = encryptionService.encrypt(dto.getValue());
      ProjectEnvVar envVar =
          projectEnvVarRepository
              .findByProjectIdAndKey(projectId, dto.getKey())
              .orElseGet(() -> projectEnvVarMapper.toEntity(dto.getKey(), encrypted, project));
      envVar.setEncryptedValue(encrypted);
      projectEnvVarRepository.save(envVar);
    }

    return getEnvVars(projectId, userId);
  }

  @Transactional
  public ProjectEnvVarDTO updateEnvVar(
      UUID projectId, String key, UUID userId, ProjectEnvVarDTO dto) {
    resolveAndVerifyOwnership(projectId, userId);

    ProjectEnvVar envVar =
        projectEnvVarRepository
            .findByProjectIdAndKey(projectId, key)
            .orElseThrow(() -> ErrorCode.RESOURCE_NOT_FOUND.build(ENV_VAR, key));

    envVar.setEncryptedValue(encryptionService.encrypt(dto.getValue()));
    projectEnvVarRepository.save(envVar);

    return projectEnvVarMapper.toResponse(envVar.getKey(), dto.getValue());
  }

  @Transactional
  public void deleteEnvVar(UUID projectId, String key, UUID userId) {
    resolveAndVerifyOwnership(projectId, userId);

    ProjectEnvVar envVar =
        projectEnvVarRepository
            .findByProjectIdAndKey(projectId, key)
            .orElseThrow(() -> ErrorCode.RESOURCE_NOT_FOUND.build(ENV_VAR, key));

    projectEnvVarRepository.delete(envVar);
  }

  private Project resolveAndVerifyOwnership(UUID projectId, UUID userId) {
    Project project =
        projectService
            .findById(projectId)
            .orElseThrow(() -> ErrorCode.RESOURCE_NOT_FOUND.build(PROJECT, projectId));

    if (!project.getUser().getId().equals(userId)) {
      throw ErrorCode.RESOURCE_NOT_FOUND.build(PROJECT, projectId);
    }

    return project;
  }
}
