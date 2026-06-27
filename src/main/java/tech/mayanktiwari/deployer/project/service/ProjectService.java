package tech.mayanktiwari.deployer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.mayanktiwari.deployer.project.dto.CreateProjectDTO;
import tech.mayanktiwari.deployer.project.dto.ProjectResponseDTO;
import tech.mayanktiwari.deployer.project.entity.Project;
import tech.mayanktiwari.deployer.project.mapper.ProjectMapper;
import tech.mayanktiwari.deployer.project.repository.ProjectRepository;
import tech.mayanktiwari.deployer.users.entity.User;
import tech.mayanktiwari.deployer.users.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponseDTO createProject(CreateProjectDTO createProjectDTO, UUID userId) {
        if (projectRepository.existsByNameAndUserId(createProjectDTO.getName(), userId)) {
            return
        }

        User user = userRepository.findById(userId).orElseThrow(());
        Project project = projectMapper.toEntity(createProjectDTO, user);

        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponse(savedProject);
    }
}
