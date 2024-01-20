package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.repositories.ImageStorageRepository;
import com.example.Slearning.Backend.Java.repositories.VideoStorageRepository;
import com.example.Slearning.Backend.Java.services.FileStorageService;
import com.example.Slearning.Backend.Java.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private ImageStorageRepository imageStorageRepository;

    @Autowired
    private VideoStorageRepository videoStorageRepository;

    @Override
    public String uploadFile(String pathServer, MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();

        String randomID = UUID.randomUUID().toString();
        String fileName = randomID.concat(originalName.substring(originalName.lastIndexOf(".")));

        String filePath = pathServer + File.separator + fileName;

        File f = new File(pathServer);
        if(!f.exists())
            f.mkdir();

        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }

    @Override
    public InputStream getFileResource(String pathServer, String fileName) throws IOException {
        String fullPath = pathServer + File.separator + fileName;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
