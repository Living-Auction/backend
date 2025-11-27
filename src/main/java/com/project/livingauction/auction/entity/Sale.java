package com.project.livingauction.auction.entity;

import com.project.livingauction.common.entity.BaseCreatedEntity;
import com.project.livingauction.user.entity.User;

import jakarta.persistence.CascadeType;
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
@Table(name = "sales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sale extends BaseCreatedEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", columnDefinition = "BINARY(16)")
    private AuctionItem item;

    @ManyToOne
    @JoinColumn(name = "buyer_id", columnDefinition = "BINARY(16)")
    private User buyer;

    private Integer finalPrice;

    @Column(length = 50)
    private String paymentStatus;

    @Column(length = 50)
    private String result;
}
