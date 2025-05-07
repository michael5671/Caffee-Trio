package com.ngntu10.service.Token;

import com.ngntu10.entity.JwtToken;
import com.ngntu10.exception.NotFoundException;
import com.ngntu10.repository.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {
    private final JwtTokenRepository jwtTokenRepository;

    /**
     * Find a JWT token by user id and refresh token.
     *
     * @param id           UUID
     * @param refreshToken String
     * @return JwtToken
     */
    public JwtToken findByUserIdAndRefreshToken(UUID id, String refreshToken) {
        return jwtTokenRepository.findByUserIdAndRefreshToken(id, refreshToken)
            .orElseThrow(() -> new NotFoundException("not_found_with_param"));
    }

    /**
     * Find a JWT token by token or refresh token.
     *
     * @param token String
     * @return JwtToken
     */
    public JwtToken findByTokenOrRefreshToken(String token) {
        return jwtTokenRepository.findByTokenOrRefreshToken(token, token)
            .orElseThrow(() -> new NotFoundException("not_found_with_param"));
    }

    /**
     * Save a JWT token.
     *
     * @param jwtToken JwtToken
     */
    public void save(JwtToken jwtToken) {
        jwtTokenRepository.save(jwtToken);
    }

    /**
     * Delete a JWT token.
     *
     * @param jwtToken JwtToken
     */
    public void delete(JwtToken jwtToken) {
        jwtTokenRepository.delete(jwtToken);
        log.info("Deleted token: {}", jwtToken);
    }
}
