package com.project.livingauction.auction.dto;

import com.project.livingauction.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SellerResponseDto {
    private final String nickName;
    private final String profileImg;
    
    public SellerResponseDto(User seller) {
        this.nickName = seller.getNickname();
        this.profileImg = seller.getProfileImg();
    }
}
