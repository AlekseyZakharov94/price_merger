package com.alzakhar.price_merger;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Builder
public class Price {
    private long id;
    private String productCode;
    private int number;
    private int depart;
    private Date begin;
    private Date end;
    private long value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price = (Price) o;
        return getNumber() == price.getNumber() && getDepart() == price.getDepart() && getValue() == price.getValue() && getProductCode().equals(price.getProductCode()) && getBegin().equals(price.getBegin()) && getEnd().equals(price.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductCode(), getNumber(), getDepart(), getBegin(), getEnd(), getValue());
    }
}
