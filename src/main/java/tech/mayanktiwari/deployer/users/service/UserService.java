package tech.mayanktiwari.deployer.users.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.mayanktiwari.deployer.users.entity.User;
import tech.mayanktiwari.deployer.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Optional<User> findById(UUID id) {
    return userRepository.findById(id);
  }

  public User createUser(String username, String email, String avatarUrl) {
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setAvatarUrl(avatarUrl);
    return userRepository.save(user);
  }
}
