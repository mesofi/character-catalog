package com.mesofi.collection.charactercatalog.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "character")
public class CharacterConfig {
    private List<String> keywordExclude;
}
