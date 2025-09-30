package com.nc.e_comm.repository;


import com.nc.e_comm.model.RefreshToken;
import com.nc.e_comm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteAllByUser(User user);
}
