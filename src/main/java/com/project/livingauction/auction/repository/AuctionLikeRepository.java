package com.project.livingauction.auction.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.livingauction.auction.entity.AuctionLike;

public interface AuctionLikeRepository extends JpaRepository<AuctionLike, UUID> {
    boolean existsByUserIdAndItemId(UUID userId, UUID itemId);
    Optional<AuctionLike> findByUserIdAndItemId(UUID userId, UUID itemId);
}
