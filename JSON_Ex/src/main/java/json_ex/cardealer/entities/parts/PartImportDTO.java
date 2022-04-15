package json_ex.cardealer.entities.parts;

import java.math.BigDecimal;

public class PartImportDTO {
    private String name;

    private BigDecimal price;

    private int quantity;

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
