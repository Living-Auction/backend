package com.project.livingauction.auction.dto;

import com.project.livingauction.auction.entity.AuctionPrice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PriceResponseDto {
    private final Integer startPrice;
    private final Integer minBidUnit;
    private final Integer currentPrice;
    
    public PriceResponseDto(AuctionPrice price) {
        this.startPrice = price.getStartPrice();
        this.minBidUnit = price.getMinBidUnit();
        this.currentPrice = price.getCurrentPrice();
    }
    
    
}
