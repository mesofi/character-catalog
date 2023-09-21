package com.mesofi.collection.charactercatalog.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CharacterFigureResponse {
    private String name;
    private BigDecimal priceBeforeTaxes;
    private BigDecimal priceAfterTaxes;
    private LocalDate announcement;
    private Boolean preorderConfirmationDate;
    private LocalDate preorderDate;
    private Boolean releaseConfirmationDate;
    private LocalDate releaseDate;
    private String url;
    private Distribution distribution;
    private LineUp lineUp;
    private Series series;
    private Group group;
    private boolean metalBody;
    private boolean oce;
    private boolean revival;
    private boolean plainCloth;
    private boolean broken;
    private boolean golden;
    private boolean gold;
    private boolean hkVersion;
    private boolean manga;
    private boolean surplice;
    private boolean set;
    private Integer anniversaryYear;
    private String remarks;
}
