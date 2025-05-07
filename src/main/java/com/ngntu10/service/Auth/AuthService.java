package com.ngntu10.service.Auth;

import com.ngntu10.dto.request.auth.LoginRequest;
import com.ngntu10.dto.response.auth.TokenExpiresInResponse;
import com.ngntu10.dto.response.auth.TokenResponse;
import com.ngntu10.entity.JwtToken;
import com.ngntu10.entity.User;
import com.ngntu10.exception.NotFoundException;
import com.ngntu10.exception.RefreshTokenExpiredException;
import com.ngntu10.security.JwtTokenProvider;
import com.ngntu10.security.JwtUserDetails;
import com.ngntu10.service.Token.JwtTokenService;
import com.ngntu10.service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ngntu10.util.Constants.TOKEN_HEADER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;

    private final JwtTokenService jwtTokenService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final HttpServletRequest httpServletRequest;

    /**
     * Authenticate user.
     *
     * @param loginRequest loginRequest
     * @return TokenResponse
     */
    public TokenResponse login(LoginRequest loginRequest) {
        log.info("Login request received: {}", loginRequest.getEmail());

        String badCredentialsMessage = "Bad credentials";
        String email;
        try {
            User user = userService.findByEmail(loginRequest.getEmail());
            email = user.getEmail();
        } catch (NotFoundException e) {
            log.error("User not found with email: {}", loginRequest.getEmail());
            throw new AuthenticationCredentialsNotFoundException(badCredentialsMessage);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            JwtUserDetails jwtUserDetails = jwtTokenProvider.getPrincipal(authentication);

            return generateTokens(UUID.fromString(jwtUserDetails.getId()), loginRequest.getRememberMe());
        } catch (NotFoundException e) {
            log.error("Authentication failed for email: {}", email);
            throw new AuthenticationCredentialsNotFoundException(badCredentialsMessage);
        }
    }

    /**
     * Refresh from bearer string.
     *
     * @param bearer String
     * @return TokenResponse
     */
    public TokenResponse refreshFromBearerString(final String bearer) {
        return refresh(jwtTokenProvider.extractJwtFromBearerString(bearer));
    }

    /**
     * Reset password by e-mail.
     *
     * @param email String
     */
    public void resetPassword(String email) {
        log.info("Reset password request received: {}", email);
        userService.sendEmailPasswordResetMail(email);
    }

    /**
     * Logout from bearer string by user.
     *
     * @param user   User
     * @param bearer String
     */
    public void logout(User user, final String bearer) {
        JwtToken jwtToken = jwtTokenService.findByTokenOrRefreshToken(
            jwtTokenProvider.extractJwtFromBearerString(bearer));

        if (!user.getId().equals(jwtToken.getUserId())) {
            log.error("User id: {} is not equal to token user id: {}", user.getId(), jwtToken.getUserId());
            throw new AuthenticationCredentialsNotFoundException("bad_credentials");
        }

        jwtTokenService.delete(jwtToken);
    }

    /**
     * Logout from bearer string by user.
     *
     * @param user User
     */
    public void logout(User user) {
        logout(user, httpServletRequest.getHeader(TOKEN_HEADER));
    }

    /**
     * Refresh token.
     *
     * @param refreshToken String
     * @return TokenResponse
     */
    private TokenResponse refresh(final String refreshToken) {
        log.info("Refresh request received: {}", refreshToken);

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            log.error("Refresh token is expired.");
            throw new RefreshTokenExpiredException();
        }

        User user = jwtTokenProvider.getUserFromToken(refreshToken);
        JwtToken oldToken = jwtTokenService.findByUserIdAndRefreshToken(user.getId(), refreshToken);
        if (oldToken != null && oldToken.getRememberMe()) {
            jwtTokenProvider.setRememberMe();
        }

        boolean rememberMe = false;
        if (oldToken != null) {
            rememberMe = oldToken.getRememberMe();
            jwtTokenService.delete(oldToken);
        }

        return generateTokens(user.getId(), rememberMe);
    }

    /**
     * Generate both access and refresh tokens.
     *
     * @param id         user identifier to set the subject for token and value for the expiring map
     * @param rememberMe Boolean option to set the expiration time for refresh token
     * @return an object of TokenResponse
     */
    private TokenResponse generateTokens(final UUID id, final Boolean rememberMe) {
        String token = jwtTokenProvider.generateJwt(id.toString());
        String refreshToken = jwtTokenProvider.generateRefresh(id.toString());
        if (rememberMe) {
            jwtTokenProvider.setRememberMe();
        }

        jwtTokenService.save(JwtToken.builder()
            .userId(id)
            .token(token)
            .refreshToken(refreshToken)
            .rememberMe(rememberMe)
            .ipAddress(httpServletRequest.getRemoteAddr())
            .userAgent(httpServletRequest.getHeader("User-Agent"))
            .tokenTimeToLive(jwtTokenProvider.getRefreshTokenExpiresIn())
            .build());
        log.info("Token generated for user: {}", id);

        return TokenResponse.builder()
            .token(token)
            .refreshToken(refreshToken)
            .expiresIn(
                TokenExpiresInResponse.builder()
                    .token(jwtTokenProvider.getTokenExpiresIn())
                    .refreshToken(jwtTokenProvider.getRefreshTokenExpiresIn())
                    .build()
            )
            .build();
    }
}
