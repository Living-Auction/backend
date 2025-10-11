package com.project.livingauction.auction.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.livingauction.auction.model.ImageUploadResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionImageService {
	
    private GcsUploader uploader = new GcsUploader();
    
    @Value("${GCP_STORAGE_BUCKET_NAME}")
    private String BUCKET_NAME;
	
	@Transactional
	public List<ImageUploadResult> registAuctionImage(List<MultipartFile> images) {
        
        List<ImageUploadResult> uploadedUrls = new ArrayList<>();
        
        long timestamp = System.currentTimeMillis();

        for (int i = 0; i < images.size(); i++) {
            MultipartFile file = images.get(i);

            if (file.isEmpty() || file.getOriginalFilename() == null) {
                continue;
            }

            try {
                String originalFilename = file.getOriginalFilename().replaceAll("\\s+", "_");
                String contentType = file.getContentType();
                
                String blobName = String.format("uploads/%d_%d_%s", timestamp, i, originalFilename);
                byte[] fileData = file.getBytes();
                
                String gcsUrl = uploader.uploadFileDirectly(
                        BUCKET_NAME,
                        blobName,
                        contentType,
                        fileData
                );
                
                uploadedUrls.add(new ImageUploadResult(blobName, gcsUrl));

            } catch (IOException e) {
                System.err.println("파일 업로드 중 오류 발생: " + file.getOriginalFilename() + " - " + e.getMessage());
            }
        }
        
        return uploadedUrls;
	}
	
    @Transactional
    public boolean deleteAuctionImage(String blobName) {
        return uploader.deleteFile(BUCKET_NAME, blobName);
    }
	
}
