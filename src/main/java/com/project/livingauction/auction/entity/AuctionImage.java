package com.project.livingauction.auction.entity;

import com.project.livingauction.common.entity.BaseIdEntity;

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
@Table(name = "auction_images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuctionImage extends BaseIdEntity {

    @ManyToOne
    @JoinColumn(name = "item_id", columnDefinition = "BINARY(16)")
    private AuctionItem item;

    @Column(columnDefinition = "TEXT")
    private String url;
    
    @Column(columnDefinition = "TEXT")
    private String blobName;
}
