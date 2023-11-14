package com.mm.accountstatementparser.dto;

import com.mm.accountstatementparser.entity.EntityParent;
import java.util.List;

public abstract class DtoParent {
  public abstract EntityParent toEntity();

  public abstract List<Object> toData();
}
