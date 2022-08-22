package com.mesofi.collection.charactercatalog.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mesofi.collection.charactercatalog.constraints.Amount;
import com.mesofi.collection.charactercatalog.constraints.ReleaseDate;

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

    @NotBlank(message = "is required with at least 2 characters")
    @Size(min = 2, max = 200, message = "must be between 2 and 200")
    private String name;

    @ReleaseDate(message = "is required yyyy-MM-dd and should not be less than 2003-11-01 or greater than 6 months from now")
    private Date releaseDate;

    @Amount(message = "is required and must have a positive value")
    private BigDecimal basePrice;

    @Transient
    private BigDecimal price;

    @Amount(message = "is required and must have a decimal value")
    private BigDecimal tax;

    @NotNull(message = "is required")
    private LineUp lineUp;

    @NotNull(message = "is required")
    private Distribution distribution;

    private String url;
    @Valid
    private List<Restock> restocks;
    private List<Tag> tags;
}
