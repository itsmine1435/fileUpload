package com.fileUpload.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.fileUpload.controller.FileUploadController;
import com.fileUpload.database.DocumentDAO;
import com.fileUpload.database.ImageDAO;
import com.fileUpload.demo.FileStorageProperties;
import com.fileUpload.exception.FileStorageException;
import com.fileUpload.exception.MyFileNotFoundException;
import com.fileUpload.model.Document;
import com.fileUpload.model.Image;

@Service
@Transactional
public class FileStorageService {

	private final Path fileStorageLocation;
	
	String folder;
	
	private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

	@Autowired
	private DocumentDAO documentDAO;

	@Autowired
	private ImageDAO imageDAO;

	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		logger.info("fileStorageLocation", fileStorageLocation);

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public String addDocument(MultipartFile file, String title, String decsription) {
		String result = null;
		
		Document document = createDocument(title, decsription);
		logger.info("document{}", document.toString());

		Path newpath = getPath(document , file);
		logger.info("path{}", newpath.toString());

		// Path targetLocation = this.fileStorageLocation.resolve(fileName);
		if(newpath != null)
		{
			List<Image> images = new ArrayList<Image>();
			saveImagePath(newpath,document,images,file);
			result = "Successfully uploaded";
		}
		
		return result;

	}

	private Document createDocument(String title, String decsription) {
		logger.info("at create document {}", title + decsription);
		Document document = new Document();
		document.setTitle(title);
		document.setDecsription(decsription);
		document.setDate(new Date());
		documentDAO.save(document);
		return document;
	}

	private Image saveImagePath(Path path, Document document, List<Image> images,MultipartFile file) {
		logger.info("at saveImagePath{}", path.toAbsolutePath());
		Image image = new Image();
		image.setImagePath(path.toString());
		image.setDocument(document);
		imageDAO.save(image);
		copyImage(path,file,image.getId());
		images.add(image);
		document.setImage(images);
		documentDAO.saveOrUpdate(document);
		return image;
	}

	public String uploadImage(MultipartFile file, Long documentId) {
		
		Document document = documentDAO.findOne(documentId);
		
		if(document !=null)
		{
			Path newpath = getPath(document , file);
			logger.info("path{}", newpath.toString());
			List<Image> images = document.getImage();
			saveImagePath(newpath,document,images,file);
			return "Successfully uploaded";
		}
		
		return "Failed to upload";
	}

	private Path getPath(Document document, MultipartFile file) {
		Path path = null;
		try {
			folder = "D:\\images\\" + document.getId();
			File newFile = new File(folder);
			if (newFile.exists()) {
				logger.info("folder already exists");
				path = Paths.get(folder).resolve(file.getOriginalFilename());
				//Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} else {
				newFile.mkdir();
				path = Paths.get(folder).resolve(file.getOriginalFilename());
				logger.info("newPath{}", path.toString());
				//Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (Exception e) {
			logger.info("exc {}", e);
		}
		return path;
	}
	
	private void copyImage(Path path,MultipartFile file, Long id) {
		try {
			String filename = id + "_" + file.getOriginalFilename();
			path = Paths.get(folder).resolve(filename);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			logger.info("exc {}", e);
		}
		
	}


}