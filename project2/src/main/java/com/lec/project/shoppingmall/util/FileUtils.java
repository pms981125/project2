package com.lec.project.shoppingmall.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class FileUtils {
	
	@Value("${com.lec.upload.path}")
	private String uploadPath;
	
	 // 날짜별 폴더 생성
    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);
        
        File uploadPathFolder = new File(uploadPath, folderPath);
        
        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        
        return folderPath;
    }

    // 원본 이미지 저장
    public String saveOriginalFile(MultipartFile file) throws IOException {
		   String folderPath = makeFolder();
		   // 경로 구분자를 '/'로 통일
		   folderPath = folderPath.replace("\\", "/");
		   
		   // UUID를 사용한 고유한 파일명 생성
		   String uuid = UUID.randomUUID().toString();
		   String originalFileName = file.getOriginalFilename();
		   String savedFileName = uuid + "_" + originalFileName;
		   
		   // 최종 저장 경로 ('/'로 통일)
		   String savePath = folderPath + "/" + savedFileName;
		   
		   // 실제 파일 저장 (덮어쓰기 옵션 추가)
		   Path targetPath = Paths.get(uploadPath, folderPath, savedFileName);
		   Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
		   
		   return savePath;
		}

    // 썸네일 이미지 생성
    public String createThumbnail(String originalImagePath) throws IOException {
        String thumbnailPath = originalImagePath.replace(".", "_thumb.");
        String fullPath = uploadPath + File.separator + originalImagePath;
        String fullThumbnailPath = uploadPath + File.separator + thumbnailPath;
        
        // 썸네일 생성 (400x400 크기로 고정)
        Thumbnails.of(fullPath)
            .size(400, 400)
            .toFile(fullThumbnailPath);
        
        return thumbnailPath;
    }

    // 파일 삭제
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(uploadPath + File.separator + filePath);
        Files.deleteIfExists(path);
    }

    // 이미지 파일 여부 확인
    public boolean isImageFile(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            return contentType != null && contentType.startsWith("image");
        } catch (Exception e) {
            return false;
        }
    }

    // 파일 확장자 확인
    public boolean isAllowedExtension(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
        
        for (String allowedExt : allowedExtensions) {
            if (extension.equals(allowedExt)) {
                return true;
            }
        }
        
        return false;
    }
}