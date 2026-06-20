package tech.mayanktiwari.deployer.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tech.mayanktiwari.deployer.common.config.AuthProvider;
import tech.mayanktiwari.deployer.users.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
        name = "user_auth_provider",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_provider_provider_id",
                        columnNames = {"provider", "provider_id"}
                )
        },
        indexes = {
                @Index(name = "idx_provider_provider_id", columnList = "provider, provider_id"),
                @Index(name = "idx_user_auth_provider_user_id", columnList = "user_id")
        }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAuthProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AuthProvider provider;

    @Column(nullable = false)
    String providerId;

    String accessToken;
    String refreshToken;
    LocalDateTime tokenExpiresAt;
    String scope;

    String providerUsername;
    String providerAvatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;
}
