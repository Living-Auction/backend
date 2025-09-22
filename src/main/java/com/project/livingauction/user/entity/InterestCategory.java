package com.project.livingauction.user.entity;

import com.project.livingauction.auction.entity.Category;

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
@Table(name = "interest_categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InterestCategory extends BaseCreatedEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", columnDefinition = "BINARY(16)")
    private Category category;
}
