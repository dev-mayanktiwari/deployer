package tech.mayanktiwari.deployer.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.mayanktiwari.deployer.users.entity.User;
import tech.mayanktiwari.deployer.users.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
