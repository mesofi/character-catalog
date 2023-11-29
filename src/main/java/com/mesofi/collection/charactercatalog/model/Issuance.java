/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 24, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * Issuance properties.
 * 
 * @author armandorivasarzaluz
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Issuance {

    private BigDecimal basePrice; // The price without taxes.

    private BigDecimal releasePrice; // <== Calculated == The price with taxes.

    private LocalDate firstAnnouncementDate; // Date when the figure was first announced.

    private LocalDate preorderDate; // Date when the figure was set to pre-order.

    private Boolean preorderConfirmationDay; // <== Calculated == Used to determine if the day was confirmed.

    private LocalDate releaseDate; // Date when the figure was released in Japan.

    private Boolean releaseConfirmationDay; // <== Calculated == Used to determine if the day was confirmed.

}
