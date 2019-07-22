package com.fileUpload.model;

import org.springframework.web.multipart.MultipartFile;

public class DocumentDTO {
	
	private String title;
	private String decsription; 
	private MultipartFile file;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDecsription() {
		return decsription;
	}
	public void setDecsription(String decsription) {
		this.decsription = decsription;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	@Override
	public String toString() {
		return "DocumentDTO [title=" + title + ", decsription=" + decsription + ", file=" + file + "]";
	}
	
	

}
