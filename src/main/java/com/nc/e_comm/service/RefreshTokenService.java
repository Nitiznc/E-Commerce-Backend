package com.nc.e_comm.service;

import com.nc.e_comm.exception.TokenExpiredException;
import com.nc.e_comm.exception.TokenRevokedException;
import com.nc.e_comm.model.RefreshToken;
import com.nc.e_comm.model.User;
import com.nc.e_comm.repository.RefreshTokenRepository;
import com.nc.e_comm.repository.UserRepository;
import com.nc.e_comm.security.jwt.JwtProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, JwtProvider jwtProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setToken(jwtProvider.createRefreshToken());
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusMillis(jwtProvider.getRefreshTokenMs()));
        token.setRevoked(false);
        return refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyNotExpiredAndNotRevoked(RefreshToken token){
        if(token.isRevoked()) throw new TokenRevokedException("Refresh token revoked");
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenExpiredException("Refresh token expired");
        }
        return token;
    }

    public void revokedByUser(User user){
        refreshTokenRepository.deleteAllByUser(user);
    }

    public void revoke(RefreshToken token){
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

}
