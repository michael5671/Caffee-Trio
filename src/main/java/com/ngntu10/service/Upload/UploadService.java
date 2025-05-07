package com.ngntu10.service.Upload;

import com.cloudinary.Cloudinary;
import com.ngntu10.dto.response.Cloudinary.CloudinaryResponse;
import com.ngntu10.exception.FileUploadException;
import com.ngntu10.util.FileUploadUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final Cloudinary cloudinary;
    @Value("${cloudinary.api_key}")
    
    private String API_KEY;
    @Transactional
    public CloudinaryResponse uploadImage(final MultipartFile file) {
        try {
            FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
            final String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
            final CloudinaryResponse response = this.uploadFile(file, fileName);
            return response;
        } catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    public CloudinaryResponse uploadFile(final MultipartFile file, final String fileName) {
        try {
            final Map result   = cloudinary.uploader()
                    .upload(file.getBytes(),
                            Map.of("public_id",
                                    "ngntu10/"
                                            + fileName));
            final String url      = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder().publicId(publicId).url(url)
                    .build();

        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
    }
}
