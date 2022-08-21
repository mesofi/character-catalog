package com.mesofi.collection.charactercatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.mesofi.collection.charactercatalog.config.CharacterConfig;

@SpringBootApplication
@EnableConfigurationProperties(CharacterConfig.class)
public class CharacterCatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CharacterCatalogApplication.class, args);
    }
}
