package com.fileUpload.database;

import org.springframework.stereotype.Repository;

import com.fileUpload.model.Document;
import com.fileUpload.model.Image;

@Repository
public class ImageDAOImpl extends GenericDAOImpl<Image, Long> implements ImageDAO {

}
