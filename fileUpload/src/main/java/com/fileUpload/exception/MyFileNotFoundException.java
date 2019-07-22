package com.fileUpload.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class MyFileNotFoundException extends RuntimeException {
    public MyFileNotFoundException(String message) {
        super(message);
    }

    public MyFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
