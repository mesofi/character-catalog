package com.mesofi.collection.charactercatalog.model;

import java.math.BigDecimal;
import java.util.Date;

import com.mesofi.collection.charactercatalog.constraints.ReleaseDate;

import lombok.Data;

@Data
public class Restock {
    @ReleaseDate(message = "is required yyyy-MM-dd and should not be less than 2003-11-01 or a future date", failOnFutureDate = true)
    private Date releaseDate;
    private BigDecimal basePrice;
    private Distribution distribution;
    private String url;
    private String remarks;
}
