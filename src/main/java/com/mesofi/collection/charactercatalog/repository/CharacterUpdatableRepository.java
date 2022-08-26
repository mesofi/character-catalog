package com.mesofi.collection.charactercatalog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Restock;
import com.mongodb.client.result.UpdateResult;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CharacterUpdatableRepository {
    private final MongoTemplate mongoTemplate;

    public Optional<UpdateResult> updateRestocks(String id, List<Restock> restoks) {
        Query query = new Query(Criteria.where("id").is(id));

        Update update = new Update();
        update.set("restocks", restoks);

        return Optional.ofNullable(mongoTemplate.updateFirst(query, update, CharacterFigure.class));
    }
}
