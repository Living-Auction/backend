package com.project.livingauction.auction.entity;

import java.time.LocalDateTime;

import com.project.livingauction.common.entity.BaseTimeEntity;
import com.project.livingauction.user.entity.User;

import jakarta.persistence.Column;
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
@Table(name = "auction_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuctionItem extends BaseTimeEntity {

    @ManyToOne
    @JoinColumn(name = "seller_id", columnDefinition = "BINARY(16)")
    private User seller;

    @Column(length = 20)
    private String auctionType;

    @Column(length = 255)
    private String thumbnailUrl;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", columnDefinition = "BINARY(16)")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "location_id", columnDefinition = "BINARY(16)")
    private Location location;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Column(length = 50)
    private String status;
}