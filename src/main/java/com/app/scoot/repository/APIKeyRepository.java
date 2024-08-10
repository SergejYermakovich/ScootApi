package com.app.scoot.repository;

import com.app.scoot.entity.APIKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface APIKeyRepository  extends JpaRepository<APIKey, Long> {
    Optional<APIKey> findByApiKey(String apiKey);
}
