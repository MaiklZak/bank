package com.app.bank.service;

import com.app.bank.error.InvalidFieldsException;
import com.app.bank.error.NoSuchEntityException;

import java.util.List;
import java.util.UUID;

public interface EntityService<T> {

    UUID save(T t) throws InvalidFieldsException;
    List<T> getAll();
    T getById(UUID id) throws NoSuchEntityException;
    void deleteById(UUID id);
    void update(T t) throws NoSuchEntityException;
}
