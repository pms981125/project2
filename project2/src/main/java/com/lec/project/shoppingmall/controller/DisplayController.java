package com.lec.project.shoppingmall.controller;

import java.net.URLEncoder;
import org.springframework.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
public class DisplayController {

	@Value("${com.lec.upload.path}")
	private String uploadPath;
	
	@GetMapping("/display")
	public ResponseEntity<Resource> display(@RequestParam(name = "filename") String filename) {
		try {
			filename = filename.replace("\\", "/");
			
			Path path = Paths.get(uploadPath, filename);
			String contentType = Files.probeContentType(path);
			Resource resource = new FileSystemResource(path);
			
			if(!resource.exists()) {
			return ResponseEntity.notFound().build();
			}
			
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="
							+ URLEncoder.encode(resource.getFilename(), "UTF-8"))
					.body(resource);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
}
