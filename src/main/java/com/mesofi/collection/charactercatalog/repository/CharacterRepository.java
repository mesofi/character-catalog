package com.mesofi.collection.charactercatalog.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;

public interface CharacterRepository extends MongoRepository<CharacterFigure, Integer> {
	List<CharacterFigure> findAllByOrderByReleaseDate();
}
