package com.project.livingauction.auction.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageUploadResult {
    private final String blobName;
    private final String url;
}
