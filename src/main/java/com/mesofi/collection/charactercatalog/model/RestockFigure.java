/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * The object used to store the figure re-stock.
 *
 * @author armandorivasarzaluz
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestockFigure extends Figure {

}
