package com.project.livingauction.user.entity;

import com.project.livingauction.common.entity.BaseCreatedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "social_accounts",uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "provider_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SocialAccount extends BaseCreatedEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // google, kakao
    @Column(nullable = false)
    private String provider;
    
    // provider별 unique ID
    @Column(name = "provider_id", nullable = false)
    private String providerId;
    
}
