package com.mesofi.collection.charactercatalog.mock;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

@Repository
public class CharacterFigureCustomRepositoryImpl implements CharacterFigureRepository {
    @Override
    public <S extends CharacterFigureEntity> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends CharacterFigureEntity> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends CharacterFigureEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends CharacterFigureEntity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends CharacterFigureEntity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends CharacterFigureEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends CharacterFigureEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends CharacterFigureEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends CharacterFigureEntity, R> R findBy(Example<S> example,
            Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends CharacterFigureEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends CharacterFigureEntity> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<CharacterFigureEntity> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<CharacterFigureEntity> findAll() {
        return null;
    }

    @Override
    public List<CharacterFigureEntity> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(CharacterFigureEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends CharacterFigureEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<CharacterFigureEntity> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<CharacterFigureEntity> findAll(Pageable pageable) {
        return null;
    }
}
