package com.fileUpload.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

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
    
    private Path newpath;
    
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    
    @Autowired
    private DocumentDAO documentDAO;
    
    @Autowired
    private ImageDAO imageDAO;

   @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        
        logger.info("fileStorageLocation",fileStorageLocation);

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file,String title,String decsription) {
    	String result;
    	String folder;
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        logger.info("fileName{}" , fileName);
        
        Document document = createDocument(title,decsription);
        logger.info("document{}" , document.toString());

      
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
            	result = "Failed to save";
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            //Path targetLocation = this.fileStorageLocation.resolve(fileName);
            try
            {
            	folder = "D:\\images\\" + document.getId() + "_";	
            File newFile = new File(folder);
            if(newFile.exists())
            {
            	 logger.info("folder already exists");	
            	 result = "Failed to save";
            }
            else
            {
            newFile.mkdir();
            newpath = Paths.get(folder).resolve(fileName);
            logger.info("newPath{}",newpath);
            saveImagePath(newpath,document);
            Files.copy(file.getInputStream(), newpath, StandardCopyOption.REPLACE_EXISTING);
            result = "Successfully uploaded";
            }
           
            }
            catch(Exception e)
            {
            	result = "Failed to save";
            	logger.info("exc {}",e);
            }
            return result;
        
    }

	public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

	private Document createDocument(String title, String decsription) {
		logger.info("at create document {}",title + decsription);
		Document document = new Document();
		document.setTitle(title);
		document.setDecsription(decsription);
		document.setDate(new Date());
		documentDAO.save(document);
		return document;
	}

	private void saveImagePath(Path targetLocation, Document document) {
		logger.info("at saveImagePath{}",targetLocation.toAbsolutePath());
		Image image = new Image();
		image.setImagePath(targetLocation.toString());
		image.setDocument(document);
		imageDAO.save(image);

	}

}