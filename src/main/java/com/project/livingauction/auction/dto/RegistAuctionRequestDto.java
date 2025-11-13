package com.project.livingauction.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class RegistAuctionRequestDto {
    private String title;
    private String description;
//    private String categoryId;
//    private String locationId;
    private Integer endTime;

    private Integer startPrice;
    private Integer minBidUnit;

}
