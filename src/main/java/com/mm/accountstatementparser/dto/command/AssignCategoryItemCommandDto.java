package com.mm.accountstatementparser.dto.command;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignCategoryItemCommandDto {
    private UUID transactionId;
    @Nullable private String categoryItemCode;
    @Nullable private String keyword;
}
