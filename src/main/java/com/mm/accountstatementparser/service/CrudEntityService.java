package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.entityDto.DtoParent;
import com.mm.accountstatementparser.entity.EntityParent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CrudEntityService<T extends EntityParent<?>> {
    List<T> getAll();
    T getEntityById(UUID id);
    T persistEntity(T entity);
    T updateEntityById(UUID id, T updatedEntity);
    T updateFieldsInEntityById(UUID id, Map<Object, Object> fields);
    void deleteEntityById(UUID id);

}
