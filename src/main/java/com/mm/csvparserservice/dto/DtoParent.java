package com.mm.csvparserservice.dto;

import com.mm.csvparserservice.model.EntityParent;
import com.mm.csvparserservice.model.Transaction;

public abstract class DtoParent {
    public abstract EntityParent toEntity();
}
