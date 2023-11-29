package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.entityDto.DtoParent;

public abstract class EntityParent <T extends DtoParent> {
  public abstract T toDto();
}
