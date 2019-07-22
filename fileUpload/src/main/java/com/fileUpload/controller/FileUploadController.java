package com.fileUpload.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fileUpload.model.UploadFileResponse;
import com.fileUpload.service.FileStorageService;

@RestController
public class FileUploadController {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@RequestMapping("/home")
	public static void home() {
		System.out.println("at home");
	}

	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping("/uploadFile")
	public String uploadFile(@RequestParam("file") MultipartFile file ,@RequestParam("title") String title ,@RequestParam("decsription") String decsription) {
		logger.info("Uploading file{}" , "\t" +title + "\t" +decsription);
		String result = fileStorageService.storeFile(file,title,decsription);
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		return result + "\t" + fileName;
	}

}
