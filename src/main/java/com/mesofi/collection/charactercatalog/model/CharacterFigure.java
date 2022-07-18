package com.mesofi.collection.charactercatalog.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "character-figure")
public class CharacterFigure {

    @Id
    private String id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    private Date releaseDate;
    private BigDecimal basePrice;
    private LineUp lineUp;
    private Distribution distribution;
    private boolean revival;
    private boolean accesory;
    private String url;
    private List<Restock> restocks;
}
