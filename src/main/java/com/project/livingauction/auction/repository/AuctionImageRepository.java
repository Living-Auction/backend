package com.project.livingauction.auction.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.livingauction.auction.entity.AuctionImage;

@Repository
public interface AuctionImageRepository extends JpaRepository<AuctionImage, UUID>{
	Optional<AuctionImage> findById(UUID id);
	List<AuctionImage> findByItemId(UUID id);
}
