package tech.mayanktiwari.deployer.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.mayanktiwari.deployer.auth.dto.OAuthUserInfo;
import tech.mayanktiwari.deployer.auth.entity.UserAuthProvider;
import tech.mayanktiwari.deployer.auth.repository.UserAuthProviderRepository;
import tech.mayanktiwari.deployer.users.entity.User;
import tech.mayanktiwari.deployer.users.service.UserService;

@Service
@RequiredArgsConstructor
public class UserProvisioningService {

    private final UserAuthProviderRepository userAuthProviderRepository;
    private final UserService userService;

    @Transactional
    public User findOrCreateUser(OAuthUserInfo userInfo, String accessToken) {
        return userAuthProviderRepository
                .findByProviderIdAndProvider(userInfo.getProviderId(), userInfo.getProvider())
                .map(existingAuth -> userService.findById(existingAuth.getUserId())
                        .orElseThrow(() -> new IllegalStateException(
                                "User not found for auth provider entry: " + existingAuth.getUserId()
                        )))
                .orElseGet(() -> {
                    User savedUser = userService.createUser(
                            userInfo.getUsername(),
                            userInfo.getEmail(),
                            userInfo.getAvatarUrl()
                    );

                    UserAuthProvider authProvider = new UserAuthProvider();
                    authProvider.setUserId(savedUser.getId());
                    authProvider.setProvider(userInfo.getProvider());
                    authProvider.setProviderId(userInfo.getProviderId());
                    authProvider.setAccessToken(accessToken);
                    authProvider.setProviderUsername(userInfo.getUsername());
                    authProvider.setProviderAvatar(userInfo.getAvatarUrl());
                    userAuthProviderRepository.save(authProvider);

                    return savedUser;
                });
    }
}
