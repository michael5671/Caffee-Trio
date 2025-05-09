package com.ngntu10.service.Token;

import com.ngntu10.entity.PasswordResetToken;
import com.ngntu10.entity.User;
import com.ngntu10.exception.BadRequestException;
import com.ngntu10.exception.NotFoundException;
import com.ngntu10.repository.PasswordResetTokenRepository;
import com.ngntu10.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.ngntu10.util.Constants.PASSWORD_RESET_TOKEN_LENGTH;

@Service
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final Long expiresIn;

    /**
     * Email verification token constructor.
     *
     * @param passwordResetTokenRepository PasswordResetTokenRepository
     * @param expiresIn                    Long
     */
    public PasswordResetTokenService(
        PasswordResetTokenRepository passwordResetTokenRepository,
        @Value("${app.registration.password.token.expires-in}") Long expiresIn
    ) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.expiresIn = expiresIn;
    }

    /**
     * Is e-mail verification token expired?
     *
     * @param token PasswordResetToken
     * @return boolean
     */
    public boolean isPasswordResetTokenExpired(PasswordResetToken token) {
        return token.getExpirationDate().before(new Date());
    }

    /**
     * Create password reset token from user.
     *
     * @param user User
     * @return PasswordResetToken
     */
    public PasswordResetToken create(User user) {
        String newToken = String.valueOf(100000 + new Random().nextInt(900000));
        Date expirationDate = Date.from(Instant.now().plusSeconds(expiresIn));
        Optional<PasswordResetToken> oldToken = passwordResetTokenRepository.findByUserId(user.getId());
        PasswordResetToken passwordResetToken;

        if (oldToken.isPresent()) {
            passwordResetToken = oldToken.get();
            passwordResetToken.setToken(newToken);
            passwordResetToken.setExpirationDate(expirationDate);
        } else {
            passwordResetToken = PasswordResetToken.builder()
                .user(user)
                .token(newToken)
                .expirationDate(Date.from(Instant.now().plusSeconds(expiresIn)))
                .build();
        }

        return passwordResetTokenRepository.save(passwordResetToken);
    }

    /**
     * Get password reset token by token.
     *
     * @param token String
     * @return PasswordResetToken
     */
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
            .orElseThrow(() -> new NotFoundException("not_found_with_param"));
    }

    /**
     * Get password reset token by token.
     *
     * @param token String
     * @return User
     */
    public User getUserByToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
            .orElseThrow(() -> new NotFoundException("not_found_with_param"));

        if (isPasswordResetTokenExpired(passwordResetToken)) {
            throw new BadRequestException("expired_with_param");
        }

        return passwordResetToken.getUser();
    }

    /**
     * Delete password reset token by user ID.
     *
     * @param userId UUID
     */
    public void deleteByUserId(UUID userId) {
        passwordResetTokenRepository.deleteByUserId(userId);
    }
}
