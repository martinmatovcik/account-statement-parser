package com.mm.accountstatementparser.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
public class AssignItemCommandDto {
    private String itemCode;
}
