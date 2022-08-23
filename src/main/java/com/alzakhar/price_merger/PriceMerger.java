package com.alzakhar.price_merger;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PriceMerger {

    public Set<Price> merge(Set<Price> oldPrices, Set<Price> newPrices) {
        Map<String, Price> priceMap = oldPrices.stream().collect(Collectors.toMap(this::getCompositeKey, Function.identity()));
        newPrices.forEach(price -> {
            Price existingPrice = priceMap.get(getCompositeKey(price));

            if (existingPrice == null || !findIntevalOverlap(existingPrice, price)) {
                priceMap.put(price.getProductCode(), price);
            } else {
                if (existingPrice.getValue() == price.getValue()) {
                    existingPrice.setBegin(price.getBegin());
                    existingPrice.setEnd(price.getEnd());
                } else {
                    price.setNumber(existingPrice.getNumber() + 1);
                    existingPrice.setEnd(price.getBegin());
                    priceMap.put(price.getProductCode(), price);
                }
            }
        });


        return new HashSet<>(priceMap.values());
    }

    private boolean findIntevalOverlap(Price price1, Price price2) {
        Date begin1 = price1.getBegin();
        Date end1 = price1.getEnd();
        Date begin2 = price2.getBegin();
        Date end2 = price2.getEnd();
        return (begin1.before(end2) && end1.after(begin2));
    }

    private String getCompositeKey(Price price) {
        return String.join("_", price.getProductCode(), Integer.toString(price.getDepart()));
    }
}
