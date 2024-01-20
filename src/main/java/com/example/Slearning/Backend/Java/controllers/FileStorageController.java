package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.services.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/v1/files")
public class FileStorageController {
    @Value("${project.images}")
    private String imagePath;

    @Value("${project.videos}")
    private String videoPath;

    @Value(("${project.attachFiles}"))
    private String attachFilePath;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping(value = "/download/{imageUrl}", produces = MediaType.IMAGE_PNG_VALUE)
    public void getImageResource(@PathVariable String imageUrl, HttpServletResponse response) throws IOException {
        InputStream resource = this.fileStorageService.getFileResource(imagePath, imageUrl);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    @GetMapping(value = "/download/file-attach/{fileAttachUrl}", produces = MediaType.APPLICATION_PDF_VALUE)
    public void getFileAttachResource(@PathVariable String fileAttachUrl, HttpServletResponse response) throws IOException {
        InputStream resource = this.fileStorageService.getFileResource(attachFilePath, fileAttachUrl);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    @GetMapping(value = "/show/video/{videoUrl}", produces = "video/mp4")
    public void getVideoResource(
            @PathVariable String videoUrl,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        InputStream resource = this.fileStorageService.getFileResource(videoPath, videoUrl);
        String range = request.getHeader("Range");
        System.err.println(request.getInputStream().available());

        if(range != null) {
            range = range.trim().substring("range=".length());
            String[] parts = range.split("-");
            long start = Long.parseLong(parts[0]);
            long end =  parts.length > 1 ? Long.parseLong(parts[1]) : resource.available() - 1;
            long contentLength = end - start + 1;
            if(start < resource.available() && end < resource.available() && end > start) {
                response.setContentType("video/mp4");
                response.setHeader("Accept-Ranges", "bytes");
                response.setHeader("Content-Range", new StringBuilder().append(start).append("-").append(end).append("/").append(resource.available()).toString());
                response.setContentLengthLong(contentLength);
                response.setHeader("X-Content-Duration", "21");
                response.setHeader("Content-Duration", "21");
                StreamUtils.copy(resource, response.getOutputStream());
            } else {
                response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            }
        } else {
            response.setContentLength(resource.available());
            response.setContentType("video/mp4");
            StreamUtils.copy(resource, response.getOutputStream());
        }
    }
}
