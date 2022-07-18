package com.mesofi.collection.charactercatalog.repository;

import java.util.List;

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
    final MongoTemplate mongoTemplate;

    public void updateRestocks(String id, List<Restock> restoks) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("restocks", restoks);

        UpdateResult result = mongoTemplate.updateFirst(query, update, CharacterFigure.class);

        if (result == null)
            System.out.println("No documents updated");
        else
            System.out.println(result.getModifiedCount() + " document(s) updated..");

    }
}
