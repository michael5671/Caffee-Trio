package com.ngntu10.service.Token;

import com.ngntu10.entity.EmailVerificationToken;
import com.ngntu10.entity.User;
import com.ngntu10.exception.BadRequestException;
import com.ngntu10.exception.NotFoundException;
import com.ngntu10.repository.EmailVerificationTokenRepository;
import com.ngntu10.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.ngntu10.util.Constants.EMAIL_VERIFICATION_TOKEN_LENGTH;

@Service
public class EmailVerificationTokenService {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    private final Long expiresIn;

    /**
     * Email verification token constructor.
     *
     * @param emailVerificationTokenRepository EmailVerificationTokenRepository
     * @param expiresIn                        Long
     */
    public EmailVerificationTokenService(
        EmailVerificationTokenRepository emailVerificationTokenRepository,
        @Value("${app.registration.email.token.expires-in}") Long expiresIn
    ) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.expiresIn = expiresIn;
    }

    /**
     * Is e-mail verification token expired?
     *
     * @param token EmailVerificationToken
     * @return boolean
     */
    public boolean isEmailVerificationTokenExpired(EmailVerificationToken token) {
        return token.getExpirationDate().before(new Date());
    }

    /**
     * Create email verification token from user.
     *
     * @param user User
     * @return EmailVerificationToken
     */
    public EmailVerificationToken create(User user) {
        String newToken = String.valueOf(100000 + new Random().nextInt(900000));
        Date expirationDate = Date.from(Instant.now().plusSeconds(expiresIn));
        Optional<EmailVerificationToken> oldToken = emailVerificationTokenRepository.findByUserId(user.getId());
        EmailVerificationToken emailVerificationToken;

        if (oldToken.isPresent()) {
            emailVerificationToken = oldToken.get();
            emailVerificationToken.setToken(newToken);
            emailVerificationToken.setExpirationDate(expirationDate);
        } else {
            emailVerificationToken = EmailVerificationToken.builder()
                .user(user)
                .token(newToken)
                .expirationDate(Date.from(Instant.now().plusSeconds(expiresIn)))
                .build();
        }

        return emailVerificationTokenRepository.save(emailVerificationToken);
    }

    /**
     * Get email verification token by token.
     *
     * @param token String
     * @return User
     */
    public User getUserByToken(String token) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token)
            .orElseThrow(() -> new NotFoundException("not found with param"));

        if (isEmailVerificationTokenExpired(emailVerificationToken)) {
            throw new BadRequestException("expired with param");
        }

        return emailVerificationToken.getUser();
    }

    /**
     * Delete email verification token by user ID.
     *
     * @param userId UUID
     */
    public void deleteByUserId(UUID userId) {
        emailVerificationTokenRepository.deleteByUserId(userId);
    }
}
