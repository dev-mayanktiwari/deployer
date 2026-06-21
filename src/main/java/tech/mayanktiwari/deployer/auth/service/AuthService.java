package tech.mayanktiwari.deployer.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.mayanktiwari.deployer.auth.dto.OAuthUserInfo;
import tech.mayanktiwari.deployer.users.entity.User;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final UserProvisioningService userProvisioningService;
  private final JwtService jwtService;

  public String handleOAuth2Login(OAuthUserInfo userInfo, String accessToken) {
    User user = userProvisioningService.findOrCreateUser(userInfo, accessToken);
    return jwtService.generateToken(user.getId(), user.getUsername());
  }
}
