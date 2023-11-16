package com.mm.accountstatementparser.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AssignItemCommandDto {
    private UUID transactionId;
    private String itemCode;
}
