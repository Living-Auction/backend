package com.project.livingauction.auction.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.livingauction.auction.dto.AuctionItemListResponseDto;
import com.project.livingauction.auction.dto.AuctionItemResponseDto;
import com.project.livingauction.auction.dto.RegistAuctionRequestDto;
import com.project.livingauction.auction.dto.TestRegistAuctionRequestDto;
import com.project.livingauction.auction.dto.UpdateAuctionRequestDto;
import com.project.livingauction.auction.entity.AuctionImage;
import com.project.livingauction.auction.entity.AuctionItem;
import com.project.livingauction.auction.entity.AuctionLike;
import com.project.livingauction.auction.entity.AuctionState;
import com.project.livingauction.auction.model.ImageUploadResult;
import com.project.livingauction.auction.repository.AuctionImageRepository;
import com.project.livingauction.auction.repository.AuctionLikeRepository;
import com.project.livingauction.auction.repository.AuctionRepository;
import com.project.livingauction.auction.repository.AuctionStateRepository;
import com.project.livingauction.user.service.CustomOAuth2UserService;
import com.project.livingauction.user.entity.User;
import com.project.livingauction.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionItemService {

    private final AuctionImageRepository auctionImageRepository;
    private final AuctionStateRepository auctionStateRepository;
    private final AuctionLikeRepository auctionLikeRepository;
	private final AuctionRepository auctionRepository;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final AuctionImageService auctionImageService;
	private final UserRepository userRepository;
	
	@Transactional
	public AuctionItemResponseDto getAuctionItem(String itemId) {
		
		AuctionState auctionInfo = auctionStateRepository.findByItemId(UUID.fromString(itemId))
				.orElseThrow(() -> 
				new NoSuchElementException("요청한 경매를 찾을 수 없습니다."));
			
		List<AuctionImage> images = auctionImageRepository.findByItemId(UUID.fromString(itemId));
		
		return AuctionItemResponseDto.fromEntity(auctionInfo , images); 
		
	}
	
	@Transactional
	public List<AuctionItemListResponseDto> getAuctionItemList() {

		List<AuctionState> auctionList = auctionStateRepository.findAll();
		
		List<AuctionItemListResponseDto> responseAuctionList = new ArrayList<>();
		
		for(AuctionState a : auctionList) {
			responseAuctionList.add(AuctionItemListResponseDto.from(a));
		}
		
		return responseAuctionList;
	}
	
	@Transactional
	public boolean registAuctionItem(RegistAuctionRequestDto auctionRequestDto, List<MultipartFile> image) {
		User user = customOAuth2UserService.getUserByAuthentication();
		
		List<ImageUploadResult> images = auctionImageService.registAuctionImage(image);
		
		AuctionItem auctionItem = AuctionItem.builder()
				.seller(user)
				.title(auctionRequestDto.getTitle())
				.description(auctionRequestDto.getDescription())
				.thumbnailUrl(images.get(0).getUrl())
				.endTime(LocalDateTime.now().plusDays(auctionRequestDto.getEndTime()))
				.build();
		
		AuctionItem ai = auctionRepository.save(auctionItem);
		
		for(ImageUploadResult res : images) {
			auctionImageRepository.save(AuctionImage.builder()
					.item(auctionItem)
					.url(res.getUrl())
					.blobName(res.getBlobName())
					.build());
		}
		
		AuctionState auctionState = AuctionState.builder()
				.item(ai)
				.startPrice(auctionRequestDto.getStartPrice())
				.minBidUnit(auctionRequestDto.getMinBidUnit())
				.currentPrice(auctionRequestDto.getStartPrice())
				.build();
		
		auctionStateRepository.save(auctionState);
		
		return true;
	}
	
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	//TEST용 로그인을 하지 않아도 가능하게 끔 생성
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	@Transactional
	public boolean testRegistAuctionItem(TestRegistAuctionRequestDto testAuctionRequestDto, List<MultipartFile> image) {
		User user = userRepository.findByEmail(testAuctionRequestDto.getUserEmail()).get();
		
		List<ImageUploadResult> images = auctionImageService.registAuctionImage(image);
		
		AuctionItem auctionItem = AuctionItem.builder()
				.seller(user)
				.title(testAuctionRequestDto.getTitle())
				.description(testAuctionRequestDto.getDescription())
				.thumbnailUrl(images.get(0).getUrl())
				.endTime(LocalDateTime.now().plusDays(testAuctionRequestDto.getEndTime()))
				.build();
		
		AuctionItem ai = auctionRepository.save(auctionItem);
		
		for(ImageUploadResult res : images) {
			auctionImageRepository.save(AuctionImage.builder()
					.item(auctionItem)
					.url(res.getUrl())
					.blobName(res.getBlobName())
					.build());
		}
		
		AuctionState auctionState = AuctionState.builder()
				.item(ai)
				.startPrice(testAuctionRequestDto.getPrices().getStartPrice())
				.minBidUnit(testAuctionRequestDto.getPrices().getMinBidUnit())
				.currentPrice(testAuctionRequestDto.getPrices().getStartPrice())
				.build();
		
		auctionStateRepository.save(auctionState);
		
		return true;
	}
	
	
	// update delete close 에 우선적으로 로그인 없이 게시글 id 로만 삭제 가능하게 만듬
	@Transactional
	public boolean updateAuctionItem(String id , UpdateAuctionRequestDto updateAuctionRequestDto, List<MultipartFile> image) {
		User user = customOAuth2UserService.getUserByAuthentication();
		
		AuctionItem auctionItem = auctionRepository.findById(UUID.fromString(id)).get();
		
		if(user.getId() != auctionItem.getSeller().getId()) {
			return false;
		}
		
		List<AuctionImage> imageData = auctionImageRepository.findByItemId(UUID.fromString(id));
		
		for(AuctionImage i : imageData) {
			auctionImageService.deleteAuctionImage(i.getBlobName());
		}
			
		List<ImageUploadResult> images = auctionImageService.registAuctionImage(image);
		
		auctionItem.update(updateAuctionRequestDto, images.get(0).getUrl());
		
		for(ImageUploadResult res : images) {
			auctionImageRepository.save(AuctionImage.builder()
					.item(auctionItem)
					.url(res.getUrl())
					.blobName(res.getBlobName())
					.build());
		}
		
		return true;
	}
	
	@Transactional
	public void deleteAuctionItem(String id) {
		User user = customOAuth2UserService.getUserByAuthentication();
		
		AuctionItem auctionItem = auctionRepository.findById(UUID.fromString(id)).get();
		
		if(user.getId() == auctionItem.getSeller().getId()) {
			List<AuctionImage> imageData = auctionImageRepository.findByItemId(UUID.fromString(id));
			
			for(AuctionImage image : imageData) {
				auctionImageService.deleteAuctionImage(image.getBlobName());			
			}
			
			auctionRepository.deleteById(UUID.fromString(id));
		}
		
	}

//	@Transactional
//	public void closeAuctionItem(String id) {
//		
//	}
	
	@Transactional(readOnly = true)
	public List<AuctionItemListResponseDto> getLatestCreated() {				
	    return auctionStateRepository.findAllOrderByCreatedAtDesc()
	            .stream()
	            .map(AuctionItemListResponseDto::from)
	            .toList();
	}
	
	@Transactional(readOnly = true)
	public List<AuctionItemListResponseDto> getLikedItem() {		
        return auctionStateRepository.findAllOrderByLikeCountDesc()
                .stream()
                .map(AuctionItemListResponseDto::from)
                .toList();
	}
	
	@Transactional(readOnly = true)
	public List<AuctionItemListResponseDto> getDeadlineItem() {		
        return auctionStateRepository.findAllOrderByEndTimeAsc()
                .stream()
                .map(AuctionItemListResponseDto::from)
                .toList();
	}
	
    @Transactional
    public int increaseLike(UUID itemId) {
        AuctionState state = auctionStateRepository.findByItemId(itemId).orElseThrow(() -> 
			new NoSuchElementException("요청한 경매를 찾을 수 없습니다."));
        
        User user = customOAuth2UserService.getUserByAuthentication();
        

        if (!auctionLikeRepository.existsByUserIdAndItemId(user.getId(), itemId)) {
            AuctionLike like = AuctionLike.builder()
                    .user(user)
                    .item(state.getItem())
                    .build();
            auctionLikeRepository.save(like);
            
            auctionStateRepository.incrementLikeCountByItemId(itemId);
        }
        
        AuctionState refreshed = auctionStateRepository.findByItemId(itemId).orElseThrow(() ->
        	new NoSuchElementException("요청한 경매를 찾을 수 없습니다."));
        
        return refreshed.getLikeCount();
    }

    @Transactional
    public int decreaseLike(UUID itemId) {
        AuctionState state = auctionStateRepository.findByItemId(itemId).orElseThrow(() -> 
			new NoSuchElementException("요청한 경매를 찾을 수 없습니다."));
        User user = customOAuth2UserService.getUserByAuthentication();
        

        if (auctionLikeRepository.existsByUserIdAndItemId(user.getId(), itemId)) {
            AuctionLike like = auctionLikeRepository.findByUserIdAndItemId(user.getId(), itemId)
                    .orElseThrow(() -> new NoSuchElementException("좋아요를 취소할수 없습니다."));
            auctionLikeRepository.delete(like);
            
            auctionStateRepository.decrementLikeCountByItemId(itemId);
        }
        
        AuctionState refreshed = auctionStateRepository.findByItemId(itemId).orElseThrow(() ->
        	new NoSuchElementException("요청한 경매를 찾을 수 없습니다."));
        return refreshed.getLikeCount();
    }
    
    @Transactional
    public int like(UUID itemId) {
        AuctionState state = auctionStateRepository.findByItemId(itemId).orElseThrow(() -> 
			new NoSuchElementException("요청한 경매를 찾을 수 없습니다."));
        User user = customOAuth2UserService.getUserByAuthentication();
        
        if (auctionLikeRepository.existsByUserIdAndItemId(user.getId(), itemId)) {
            AuctionLike like = auctionLikeRepository.findByUserIdAndItemId(user.getId(), itemId)
                    .orElseThrow(() -> new NoSuchElementException("좋아요를 취소할수 없습니다."));
            auctionLikeRepository.delete(like);
            
            auctionStateRepository.decrementLikeCountByItemId(itemId);
        }else {
            AuctionLike like = AuctionLike.builder()
                    .user(user)
                    .item(state.getItem())
                    .build();
            auctionLikeRepository.save(like);
            
            auctionStateRepository.incrementLikeCountByItemId(itemId);
        }
        
        AuctionState refreshed = auctionStateRepository.findByItemId(itemId).orElseThrow(() ->
        	new NoSuchElementException("요청한 경매를 찾을 수 없습니다."));
        return refreshed.getLikeCount();
    }
}
