package com.mesofi.collection.charactercatalog.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mesofi.collection.charactercatalog.model.LineUp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterFigureView {
    private String id;
    private String displayableName;
    private BigDecimal releasePrice;

    private LocalDate preorderDate;
    private boolean preorderConfirmationDay;
    private LocalDate releaseDate;
    private boolean releaseConfirmationDay;

    private LineUp lineUp;
}
