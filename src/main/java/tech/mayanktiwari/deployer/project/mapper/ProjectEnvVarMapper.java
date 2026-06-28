package tech.mayanktiwari.deployer.project.mapper;

import org.springframework.stereotype.Component;
import tech.mayanktiwari.deployer.project.dto.ProjectEnvVarDTO;
import tech.mayanktiwari.deployer.project.entity.Project;
import tech.mayanktiwari.deployer.project.entity.ProjectEnvVar;

@Component
public class ProjectEnvVarMapper {

  public ProjectEnvVar toEntity(String key, String encryptedValue, Project project) {
    ProjectEnvVar projectEnvVar = new ProjectEnvVar();
    projectEnvVar.setKey(key);
    projectEnvVar.setEncryptedValue(encryptedValue);
    projectEnvVar.setProject(project);
    return projectEnvVar;
  }

  public ProjectEnvVarDTO toResponse(String key, String decryptedValue) {
    return ProjectEnvVarDTO.builder().key(key).value(decryptedValue).build();
  }
}
