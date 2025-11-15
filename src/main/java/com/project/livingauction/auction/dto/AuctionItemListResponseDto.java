package com.project.livingauction.auction.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.project.livingauction.auction.entity.AuctionImage;
import com.project.livingauction.auction.entity.AuctionState;
import com.project.livingauction.auction.entity.Category;
import com.project.livingauction.auction.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuctionItemListResponseDto {
	
	private final String id;

	private final String title;

    private final String thumbnailUrl;
    
    private final LocalDateTime endTime;
    
    private final Integer price;

    public static AuctionItemListResponseDto from(AuctionState item) {
        return AuctionItemListResponseDto.builder()
            .id(item.getId().toString())
            .title(item.getItem().getTitle())
            .thumbnailUrl(item.getItem().getThumbnailUrl())
            .price(item.getCurrentPrice())
            .endTime(item.getItem().getEndTime())
            .build();
    }
    
    
    
}
