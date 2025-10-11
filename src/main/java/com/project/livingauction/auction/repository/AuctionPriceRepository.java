package com.project.livingauction.auction.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.livingauction.auction.entity.AuctionPrice;

@Repository
public interface AuctionPriceRepository extends JpaRepository<AuctionPrice, UUID>{
	Optional<AuctionPrice> findById(UUID id);
	Optional<AuctionPrice> findByItemId(UUID itemID);
}
