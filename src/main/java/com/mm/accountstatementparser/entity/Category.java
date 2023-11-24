package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.entityDto.CategoryDto;
import com.mm.accountstatementparser.dto.entityDto.ItemDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

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
  @Builder.Default private BigDecimal plannedAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal realAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal difference = BigDecimal.ZERO;

  @OneToMany(mappedBy = "category")
  @Builder.Default
  @ToString.Exclude
  private List<Item> items = new ArrayList<>();

  @Override
  public CategoryDto toDto() {
    return CategoryDto.builder()
        .id(this.id)
        .code(this.code)
        .headerValue(this.headerValue)
        .plannedAmount(this.plannedAmount)
        .realAmount(this.realAmount)
        .difference(this.difference)
        .items(this.items != null ? this.items.stream().map(Item::toDto).toList() : null)
        .build();
  }
}
