package com.project.livingauction.auction.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.livingauction.auction.entity.AuctionItem;
import com.project.livingauction.user.entity.User;


@Repository
public interface AuctionRepository extends JpaRepository<AuctionItem, UUID>{
	
	Optional<AuctionItem> findById(UUID id);
	
	List<AuctionItem> findAllBySeller(User seller);
	
	List<AuctionItem> findAllByOrderByCreatedAtDesc();
	
	List<AuctionItem> findByEndTimeAfterOrderByEndTimeAsc(LocalDateTime time);
	
}
