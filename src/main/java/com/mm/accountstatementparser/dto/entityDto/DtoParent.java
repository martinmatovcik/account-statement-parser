package com.mm.accountstatementparser.dto.entityDto;

import com.mm.accountstatementparser.entity.EntityParent;
import java.util.List;

public abstract class DtoParent<T extends EntityParent> {
  public abstract T toEntity();

  public abstract List<Object> toData();
}
