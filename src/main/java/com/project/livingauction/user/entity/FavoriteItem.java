package com.project.livingauction.user.entity;

import com.project.livingauction.auction.entity.AuctionItem;
import com.project.livingauction.common.entity.BaseCreatedEntity;

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
@Table(name = "favorite_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FavoriteItem extends BaseCreatedEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", columnDefinition = "BINARY(16)")
    private AuctionItem item;
}
