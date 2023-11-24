package com.mm.accountstatementparser.dto.entityDto;

import com.mm.accountstatementparser.entity.Category;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto extends DtoParent {
    @Nullable private UUID id;
    private String code;
    private String headerValue;
    @Nullable @Builder.Default private BigDecimal plannedAmount = BigDecimal.ZERO;
    @Nullable @Builder.Default private BigDecimal realAmount = BigDecimal.ZERO;
    @Nullable @Builder.Default private BigDecimal difference = BigDecimal. ZERO;
    @Nullable @Builder.Default @ToString.Exclude
    private List<ItemDto> items = new ArrayList<>();

    @Override
    public Category toEntity() {
        return Category.builder()
                .id(this.id)
                .code(this.code)
                .headerValue(this.headerValue)
                .plannedAmount(this.plannedAmount)
                .realAmount(this.realAmount)
                .difference(this.difference)
                .items(this.items != null ? this.items.stream().map(ItemDto::toEntity).toList() : null)
                .build();
    }

    @Override
    public List<Object> toData() {
        return null;
    }
}
