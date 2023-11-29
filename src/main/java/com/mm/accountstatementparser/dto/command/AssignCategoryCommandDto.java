package com.mm.accountstatementparser.dto.command;

import jakarta.annotation.Nullable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignCategoryCommandDto {
  private String categoryItemCode;
  private String categoryCode;
}
