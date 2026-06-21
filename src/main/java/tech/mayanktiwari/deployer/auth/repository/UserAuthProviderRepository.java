package tech.mayanktiwari.deployer.auth.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.mayanktiwari.deployer.auth.entity.UserAuthProvider;
import tech.mayanktiwari.deployer.common.config.AuthProvider;

@Repository
public interface UserAuthProviderRepository extends JpaRepository<UserAuthProvider, UUID> {

  Optional<UserAuthProvider> findByProviderIdAndProvider(String providerId, AuthProvider provider);

  List<UserAuthProvider> findAllByUser_Id(UUID userId);
}
