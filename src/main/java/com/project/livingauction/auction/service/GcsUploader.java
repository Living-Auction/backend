package com.project.livingauction.auction.service;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Component
public class GcsUploader {

    private final Storage storage;

    public GcsUploader() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public String uploadFileDirectly(
            String bucketName,
            String blobName,
            String contentType,
            byte[] fileData) throws IOException {

        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        System.out.println("GCS에 파일 업로드 시작: gs://" + bucketName + "/" + blobName);

        storage.create(blobInfo, fileData);

        System.out.println("파일 업로드 성공");

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);
    }
    
    public boolean deleteFile(String bucketName, String blobName) {
        
        BlobId blobId = BlobId.of(bucketName, blobName);
        
        System.out.println("GCS 파일 삭제 요청: gs://" + bucketName + "/" + blobName);

        boolean deleted = storage.delete(blobId);
        
        if (deleted) {
            System.out.println("이미지 파일 삭제 성공");
        } else {
            System.out.println("이미지 삭제 실패: 파일이 존재하지 않거나 권한이 부족합니다.");
        }
        
        return deleted;
    }
}