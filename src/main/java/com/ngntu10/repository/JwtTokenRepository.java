package com.ngntu10.repository;

import com.ngntu10.entity.JwtToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JwtTokenRepository extends CrudRepository<JwtToken, UUID> {
    Optional<JwtToken> findByTokenOrRefreshToken(String token, String refreshToken);

    Optional<JwtToken> findByUserIdAndRefreshToken(UUID id, String refreshToken);
}
