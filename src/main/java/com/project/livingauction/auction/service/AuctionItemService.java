package com.project.livingauction.auction.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.livingauction.auction.dto.AuctionItemResponseDto;
import com.project.livingauction.auction.dto.RegistAuctionRequestDto;
import com.project.livingauction.auction.dto.TestRegistAuctionRequestDto;
import com.project.livingauction.auction.entity.AuctionImage;
import com.project.livingauction.auction.entity.AuctionItem;
import com.project.livingauction.auction.entity.AuctionPrice;
import com.project.livingauction.auction.model.ImageUploadResult;
import com.project.livingauction.auction.repository.AuctionImageRepository;
import com.project.livingauction.auction.repository.AuctionPriceRepository;
import com.project.livingauction.auction.repository.AuctionRepository;
import com.project.livingauction.oauth.service.CustomOAuth2UserService;
import com.project.livingauction.user.entity.User;
import com.project.livingauction.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionItemService {

    private final AuctionImageRepository auctionImageRepository;
    private final AuctionPriceRepository auctionPriceRepository;
	private final AuctionRepository auctionRepository;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final AuctionImageService auctionImageService;
	private final UserRepository userRepository;
	
	@Transactional
	public AuctionItemResponseDto getAuctionItem(String itemId) {
		
		AuctionPrice auctionInfo = auctionPriceRepository.findByItemId(UUID.fromString(itemId))
				.orElseThrow(() -> 
				new NoSuchElementException("요청한 경매를 찾을 수 없습니다."));
		
		List<AuctionImage> images = auctionImageRepository.findByItemId(UUID.fromString(itemId));
		
		return AuctionItemResponseDto.fromEntity(auctionInfo , images); 
		
	}
	
	@Transactional
	public AuctionItem getAuctionItemList(String itemId) {
		AuctionItem item = auctionRepository.findById(UUID.fromString(itemId))
				.orElseThrow(() -> 
				new NoSuchElementException("요청한 경매를 찾을 수 없습니다."));
		return item;
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
		
		AuctionPrice auctionprice = AuctionPrice.builder()
				.item(ai)
				.startPrice(auctionRequestDto.getPrices().getStartPrice())
				.minBidUnit(auctionRequestDto.getPrices().getMinBidUnit())
				.currentPrice(auctionRequestDto.getPrices().getStartPrice())
				.build();
		
		auctionPriceRepository.save(auctionprice);
		
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
		
		AuctionPrice auctionprice = AuctionPrice.builder()
				.item(ai)
				.startPrice(testAuctionRequestDto.getPrices().getStartPrice())
				.minBidUnit(testAuctionRequestDto.getPrices().getMinBidUnit())
				.currentPrice(testAuctionRequestDto.getPrices().getStartPrice())
				.build();
		
		auctionPriceRepository.save(auctionprice);
		
		return true;
	}
	
	
	// update delete close 에 우선적으로 로그인 없이 게시글 id 로만 삭제 가능하게 만듬
//	@Transactional
//	public void updateAuctionItem(String id , UpdateAuctionRequestDto updateAuctionRequestDto, List<MultipartFile> image) {
//		AuctionItem auctionItem = auctionRepository.findById(UUID.fromString(id)).get();
//		
//		List<AuctionImage> imageData = auctionImageRepository.findByItemId(UUID.fromString(id));
//		
//		for(AuctionImage image : imageData) {
//			testAuctionImageService.deleteAuctionImage(image.getBlobName());			
//		}
//		
//		List<ImageUploadResult> images = testAuctionImageService.registAuctionImage(image);
//		
//		
//		
//	}
	
	@Transactional
	public void deleteAuctionItem(String id) {
		List<AuctionImage> imageData = auctionImageRepository.findByItemId(UUID.fromString(id));
		
		for(AuctionImage image : imageData) {
			auctionImageService.deleteAuctionImage(image.getBlobName());			
		}
		
		auctionRepository.deleteById(UUID.fromString(id));
	}

//	@Transactional
//	public void closeAuctionItem(String id) {
//		
//	}
	
}
