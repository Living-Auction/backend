package com.project.livingauction.auction.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.livingauction.auction.dto.RegistAuctionRequestDto;
import com.project.livingauction.auction.dto.TestRegistAuctionRequestDto;
import com.project.livingauction.auction.repository.AuctionImageRepository;
import com.project.livingauction.auction.service.AuctionItemService;
import com.project.livingauction.result.ResultCode;
import com.project.livingauction.result.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@CrossOrigin 
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class AuctionItemController {

    private final AuctionImageRepository auctionImageRepository;
	private final AuctionItemService auctionItemService;
	
//	@Operation(summary="해당 지역 모든 경매목혹 조회")
//	@GetMapping("/location")
//	public ResultResponse location(@RequestBody  ) {
//		
//	}
	
	@Operation(summary="테스트 API")
	@GetMapping(value = "/test")
	public boolean testAPi() {
		return true;
	}
	
	@Operation(summary="경매 상세 조회")
	@GetMapping("/{id}")
	public ResponseEntity<ResultResponse> getItem(@PathVariable("id") String id) {
		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS, auctionItemService.getAuctionItem(id)));
	}
	
	@PreAuthorize("isAuthenticated()")
	@Operation(summary="경매 등록")
	@PostMapping(value = "/regist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResultResponse> registAuction(@RequestPart("auctionData") RegistAuctionRequestDto auctionRequestDto,
			@RequestPart("images") List<MultipartFile> images) {
		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS , auctionItemService.registAuctionItem(auctionRequestDto, images)));
	}
	
	@Operation(summary="경매 등록 테스트 API 로그인 X")
	@PostMapping(value = "/test" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResultResponse> testRegistAuction(@RequestPart("auctionData") TestRegistAuctionRequestDto testAuctionRequestDto,
			@RequestPart("images") List<MultipartFile> images) {
		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS , auctionItemService.testRegistAuctionItem(testAuctionRequestDto, images)));
	}
	
//	@Operation(summary="경매 수정")
//	@PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<ResultResponse> updateAuction(@PathVariable("id") String id , @RequestPart("auctionData") UpdateAuctionRequestDto updateAuctionRequestDto,
//			@RequestPart("images") List<MultipartFile> images) {
//		auctionItemService.updateAuctionItem(id , updateAuctionRequestDto);
//		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS));
//	}
	
	
	//로그인 추가 후 본인의 경매만 삭제하는 로직 추가
	@Operation(summary="경매 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<ResultResponse> deleteAuction(@PathVariable("id") String id) {
		auctionItemService.deleteAuctionItem(id);
		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS));
	}
	
//	@Operation(summary="경매 종료")
//	@PatchMapping("/{id}/close")
//	public ResponseEntity<ResultResponse> closeAuction(@PathVariable("id") String id) {
//		auctionItemService.closeAuctionItem(id);
//		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS));
//	}
	
	@Operation(summary="경매 좋아요 등록")
	@PostMapping(value = "/{id}/like")
	public ResponseEntity<ResultResponse> addLikeToItem(@PathVariable("id") String itemId) {
		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS,auctionItemService.increaseLike(UUID.fromString(itemId))));
	}
	
	@Operation(summary="경매 좋아요 제거")
	@DeleteMapping(value = "/{id}/like")
	public ResponseEntity<ResultResponse> removeLikeFromItem(@PathVariable("id") String itemId) {
		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS,auctionItemService.decreaseLike(UUID.fromString(itemId))));
	}
	
	@Operation(summary="경매 좋아요 토글방식")
	@PostMapping(value = "/{id}/likes")
	public ResponseEntity<ResultResponse> likeItem(@PathVariable("id") String itemId) {
		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS,auctionItemService.like(UUID.fromString(itemId))));
	}
	
	@Operation(summary="경매 최신순 조회")
	@GetMapping("/latest")
	public ResponseEntity<ResultResponse> latestItem() {
		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS, auctionItemService.getLatestCreated()));
	}
	
	@Operation(summary="경매 인기순 조회")
	@GetMapping("/like")
	public ResponseEntity<ResultResponse> likedItem() {
		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS, auctionItemService.getLatestCreated()));
	}
	
	@Operation(summary="경매 마감순 조회")
	@GetMapping("/deadline")
	public ResponseEntity<ResultResponse> deadlineItem() {
		return ResponseEntity.ok(ResultResponse.of(ResultCode.SUCCESS, auctionItemService.getLatestCreated()));
	}

}
