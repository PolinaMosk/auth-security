package org.inform.security.repository;

import org.inform.security.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Optional<Credentials> findByUserId(Long userId);
    Optional<Credentials> findByLogin(String login);
}
