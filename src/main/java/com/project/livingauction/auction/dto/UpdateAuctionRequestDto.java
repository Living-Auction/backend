package com.project.livingauction.auction.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAuctionRequestDto {
    private String title;
    private String description;
//    private String categoryId;
//    private String locationId;
    private Prices prices;
    private Integer endTime;
    private List<MultipartFile> images;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Prices {
        private Integer startPrice;
        private Integer minBidUnit;
    }
	
}
