package com.example.Slearning.Backend.Java.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorageService {
    String uploadImage(String pathServer, MultipartFile file) throws IOException;

    InputStream getImageResource(String pathServer, String fileName) throws IOException;


}
