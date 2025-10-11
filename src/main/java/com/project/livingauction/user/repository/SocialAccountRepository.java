package com.project.livingauction.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.livingauction.user.entity.SocialAccount;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, UUID> {
    Optional<SocialAccount> findByProviderAndProviderId(String provider, String providerId);
}
