package com.mesofi.collection.charactercatalog.views;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterFigureView {
    private String id;
    private String displayableName;
    private String originalName;
    private String baseName; 
    
    private LineUp lineUp;
    private Series series;
    private Group group;
    
    
    
    
    
    private BigDecimal releasePrice;

    private LocalDate preorderDate;
    private boolean preorderConfirmationDay;
    private LocalDate releaseDate;
    private boolean releaseConfirmationDay;

    private boolean hongKongVersion;

    
}
