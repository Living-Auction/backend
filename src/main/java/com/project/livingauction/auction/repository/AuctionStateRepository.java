package com.project.livingauction.auction.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.livingauction.auction.entity.AuctionState;

@Repository
public interface AuctionStateRepository extends JpaRepository<AuctionState, UUID>{
	Optional<AuctionState> findById(UUID id);
	Optional<AuctionState> findByItemId(UUID itemID);
    
	@Modifying
    @Transactional
    @Query("UPDATE AuctionState s SET s.likeCount = s.likeCount + 1 WHERE s.item.id = :itemId")
    int incrementLikeCountByItemId(UUID itemId);

    @Modifying
    @Transactional
    @Query("UPDATE AuctionState s SET s.likeCount = CASE WHEN s.likeCount > 0 THEN s.likeCount - 1 ELSE 0 END WHERE s.item.id = :itemId")
    int decrementLikeCountByItemId(UUID itemId);
}
