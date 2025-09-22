package com.project.livingauction.auction.entity;

import com.project.livingauction.common.entity.BaseCreatedEntity;
import com.project.livingauction.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bids")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bid extends BaseCreatedEntity {

    @ManyToOne
    @JoinColumn(name = "item_id", columnDefinition = "BINARY(16)")
    private AuctionItem item;

    @ManyToOne
    @JoinColumn(name = "bidder_id", columnDefinition = "BINARY(16)")
    private User bidder;

    private Integer price;
}