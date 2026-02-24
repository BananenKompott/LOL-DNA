package com.example.loldna.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "riot.api")
@Getter
@Setter
public class RiotApiProperties {
    private String key;
}
