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
    private State State;
    private Integer endTime;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class State {
        private Integer startPrice;
        private Integer minBidUnit;
        private Integer likeCount; 
    }
}
