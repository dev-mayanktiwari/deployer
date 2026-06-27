package tech.mayanktiwari.deployer.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.mayanktiwari.deployer.auth.dto.AuthPrincipal;
import tech.mayanktiwari.deployer.auth.dto.AuthUserResponse;
import tech.mayanktiwari.deployer.auth.dto.OAuthUserInfo;
import tech.mayanktiwari.deployer.common.exception.ErrorCode;
import tech.mayanktiwari.deployer.users.entity.User;
import tech.mayanktiwari.deployer.users.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final UserProvisioningService userProvisioningService;
  private final JwtService jwtService;
  private final UserService userService;

  public String handleOAuth2Login(OAuthUserInfo userInfo, String accessToken) {
    User user = userProvisioningService.findOrCreateUser(userInfo, accessToken);
    return jwtService.generateToken(user.getId(), user.getUsername());
  }

  public AuthUserResponse getMe(AuthPrincipal principal) {
    User user =
        userService
            .findById(principal.userId())
            .orElseThrow(() -> ErrorCode.RESOURCE_NOT_FOUND.build("User", principal.userId()));
    return new AuthUserResponse(user.getId(), user.getUsername(), user.getEmail());
  }
}
