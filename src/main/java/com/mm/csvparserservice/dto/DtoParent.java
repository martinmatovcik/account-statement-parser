package com.mm.csvparserservice.dto;

import com.mm.csvparserservice.model.EntityParent;
import java.util.List;

public abstract class DtoParent {
  public abstract EntityParent toEntity();

  public abstract List<Object> toData();
}
