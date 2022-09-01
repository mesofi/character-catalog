package com.mesofi.collection.charactercatalog.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "character")
public class CharacterConfig {
    private String symbolExclude;
    private List<String> keywordExclude;
}
