package com.mesofi.collection.charactercatalog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;

public interface CharacterRepository extends MongoRepository<CharacterFigure, String> {

    @Query("{name:'?0'}")
    Optional<CharacterFigure> findByName(String name);

    List<CharacterFigure> findAllByOrderByReleaseDate();
}
