package com.fileUpload.database;

import org.springframework.stereotype.Repository;

import com.fileUpload.model.Document;

@Repository
public class DocumentDAOImpl extends GenericDAOImpl<Document, Long> implements DocumentDAO {

}
