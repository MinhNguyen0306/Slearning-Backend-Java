package com.example.Slearning.Backend.Java.utils;

import java.util.Optional;

public class FileUtils {
    public static String getExtensionFile(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String getFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
