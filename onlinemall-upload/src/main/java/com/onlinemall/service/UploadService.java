package com.onlinemall.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public interface UploadService {

    static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg", "image/gif");

    static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    String upload(MultipartFile file);
}
