package com.mesofi.collection.charactercatalog.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Figure {

    private BigDecimal basePrice; // Base price without taxes.
    private BigDecimal officialPrice; // Price with taxes.

    private Date firstAnnouncementDate; // Date when the figure was first announced.
    private Date preOrderDate; // The preOrder date.
    private Date releaseDate; // The release date in Japan

    private Distribution distribution; // how this figure was
    private String url; // URL for the Tamashii web site.

    private String remarks;
}
