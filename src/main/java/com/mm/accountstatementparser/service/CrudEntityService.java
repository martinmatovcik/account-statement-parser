package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.EntityParent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CrudEntityService<T extends EntityParent<?>> {
    List<T> getAll();
    T getEntityById(UUID id);
    T persistEntity(T entity);
    T updateEntity(T updatedEntity);
    T updateEntityById(UUID id, T updatedEntity);
    T updateEntityFieldsById(UUID id, Map<Object, Object> fields);
    void deleteEntityById(UUID id);

}
