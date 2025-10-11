package com.project.livingauction.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.livingauction.auction.entity.Location;
import com.project.livingauction.common.entity.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder 
public class User extends BaseTimeEntity {

    @Column(nullable = false, unique = true, length = 255)
    private String email;

//    @Column(nullable = false, length = 255)
//    private String password;

    @Column(unique = true, length = 20)
    private String phone;

    @Column(nullable = false)
    private boolean isVerified = false;

    @Column(columnDefinition = "TEXT")
    private String profileImg;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;
    
    @ManyToOne
    @JoinColumn(name = "location_id", columnDefinition = "BINARY(16)")
    private Location location;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
    @JsonIgnore
    private List<SocialAccount> socialAccounts = new ArrayList<>();
    
    
}
