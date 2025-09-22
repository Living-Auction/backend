package com.project.livingauction.auction.entity;

import com.project.livingauction.common.entity.BaseCreatedEntity;
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
@Table(name = "alerts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alert extends BaseCreatedEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    private User user;

    @Column(length = 100)
    private String title;

    @Column(length = 255)
    private String content;

    @Column(length = 50)
    private String alertCategory;

    private boolean isRead = false;
}
