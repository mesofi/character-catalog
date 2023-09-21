package com.mesofi.collection.charactercatalog.controllers;

import org.mapstruct.Mapper;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.CharacterFigureResponse;

@Mapper(componentModel = "spring")
public interface CarMapper {
    
    CharacterFigureResponse toDto(CharacterFigure target);
}
