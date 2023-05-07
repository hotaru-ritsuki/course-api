package com.example.courseapi.service;

import java.io.File;
import java.io.IOException;

public interface S3Service {
    void putObject(String fileKey, byte[] file);

    byte[] getObject(String fileKey) throws IOException;
}
