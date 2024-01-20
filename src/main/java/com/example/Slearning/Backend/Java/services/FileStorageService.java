package com.example.Slearning.Backend.Java.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorageService {
    String uploadFile(String pathServer, MultipartFile file) throws IOException;

    InputStream getFileResource(String pathServer, String fileName) throws IOException;


}
