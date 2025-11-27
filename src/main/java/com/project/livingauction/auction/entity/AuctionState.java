package com.project.livingauction.auction.entity;

import com.project.livingauction.common.entity.BaseIdEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auction_state")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuctionState extends BaseIdEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", unique = true, columnDefinition = "BINARY(16)")
    private AuctionItem item;

    private Integer startPrice;
    private Integer minBidUnit;
    private Integer currentPrice;
    
    @Builder.Default
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer likeCount = 0;
    
}
