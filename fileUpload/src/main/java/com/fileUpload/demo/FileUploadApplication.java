package com.fileUpload.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.fileUpload.controller" , "com.fileUpload.service" , "com.fileUpload.database" , "com.fileUpload.model"})
@EnableConfigurationProperties({FileStorageProperties.class})
@EntityScan(basePackages= {"com.fileUpload.model"})
public class FileUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadApplication.class, args);
	}

}
