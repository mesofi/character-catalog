package com.mesofi.collection.charactercatalog.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Restock {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
	private Date releaseDate;
	private BigDecimal basePrice;
	private Distribution distribution;
	private String url;
	private String remarks;
}
