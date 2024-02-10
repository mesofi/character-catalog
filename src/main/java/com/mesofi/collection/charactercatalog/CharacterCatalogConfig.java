/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Feb 10, 2024.
 */
package com.mesofi.collection.charactercatalog;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author armandorivasarzaluz
 */
@Configuration
@EnableConfigurationProperties(CharacterCatalogConfig.Props.class)
public class CharacterCatalogConfig {

  @ConfigurationProperties(prefix = "characters")
  public record Props(List<String> namingExclusions) {}
}
