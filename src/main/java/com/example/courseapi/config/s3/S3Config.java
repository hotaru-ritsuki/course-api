package com.example.courseapi.config.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3ClientProperties s3ClientProperties;

    @Bean
    public S3Client s3Client() {
        if (s3ClientProperties.isMock()) {
            return new FakeS3();
        }
        return S3Client.builder()
                .region(Region.of(s3ClientProperties.getRegion()))
                .build();
    }
}
