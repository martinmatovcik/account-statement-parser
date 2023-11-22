package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.CategoryDto;
import com.mm.accountstatementparser.dto.DtoParent;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category extends EntityParent {
  @Id @GeneratedValue private UUID id;
  private String code;
  private String headerValue;
  private BigDecimal plannedAmount;
  @Builder.Default private BigDecimal realAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal difference = BigDecimal. ZERO;

  @OneToMany(mappedBy = "category")
  private List<Item> items;

  @Override
  public CategoryDto toDto() {
    return null;
  }
}
