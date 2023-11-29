package com.mm.accountstatementparser.dto.command;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignItemCommandDto {
    private UUID transactionId;
    @Nullable private String categoryItemCode;
    @Nullable private String keyword;
}
