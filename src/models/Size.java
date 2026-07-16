package models;

import java.math.BigDecimal;

public class Size extends BaseModel {
    private String name;
    private BigDecimal priceMultiplier;

    public Size() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPriceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(BigDecimal priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }
}
