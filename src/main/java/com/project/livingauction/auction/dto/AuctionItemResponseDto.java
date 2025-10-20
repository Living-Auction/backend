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
public class AuctionItemResponseDto {
	private final SellerResponseDto seller;
	
	private final String id;

	private final String title;

    private final String thumbnailUrl;

    private final String description;

    private final Category category;

    private final Location location;

    private final LocalDateTime startTime;
    
    private final LocalDateTime endTime;

    private final String status;
    
    private final PriceResponseDto price;
    
    private final List<String> images;

    public static AuctionItemResponseDto fromEntity(AuctionState item , List<AuctionImage> image) {
        return AuctionItemResponseDto.builder()
        	.seller(new SellerResponseDto(item.getItem().getSeller()))
            .id(item.getId().toString())
            .title(item.getItem().getTitle())
            .thumbnailUrl(item.getItem().getThumbnailUrl())
            .description(item.getItem().getDescription())
            .category(item.getItem().getCategory())
            .location(item.getItem().getLocation())
            .startTime(item.getItem().getStartTime())
            .endTime(item.getItem().getEndTime())
            .status(item.getItem().getStatus())
            .images(image.stream()
            		.map(AuctionImage::getUrl)
            		.collect(Collectors.toList()))
            .price(new PriceResponseDto(item))
            .build();
    }
    
    
    
}
