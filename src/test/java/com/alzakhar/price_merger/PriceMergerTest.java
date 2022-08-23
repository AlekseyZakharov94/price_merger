package com.alzakhar.price_merger;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Set;

public class PriceMergerTest {

    private PriceMerger merger;

    private static final String format = "yyyy-MM-dd";

    private Set<Price> existingPrices;
    private Set<Price> newPricesForNoPriceCase;
    private Set<Price> newPricesForNoOverlapCase;
    private Set<Price> newPricesForOverlapSamePricesCase;
    private Set<Price> newPricesForOverlapDifferentPricesCase;
    private Price price1;
    private Price price2;
    private Price price3;
    private Price price4;
    private Price price5;
    private Price price6;


    @Before
    @SneakyThrows
    public void setUp() {
        merger = new PriceMerger();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        price1 = Price.builder()
                .id(1)
                .productCode("100")
                .number(1)
                .depart(1)
                .begin(dateFormat.parse("2022-08-01"))
                .end(dateFormat.parse("2022-08-15"))
                .value(100)
                .build();
        price2 = Price.builder()
                .id(2)
                .productCode("110")
                .number(1)
                .depart(2)
                .begin(dateFormat.parse("2022-08-10"))
                .end(dateFormat.parse("2022-08-20"))
                .value(150)
                .build();
        price3 = Price.builder()
                .id(3)
                .productCode("120")
                .number(1)
                .depart(1)
                .begin(dateFormat.parse("2022-08-01"))
                .end(dateFormat.parse("2022-08-15"))
                .value(100)
                .build();
        price4 = Price.builder()
                .id(1)
                .productCode("100")
                .number(1)
                .depart(1)
                .begin(dateFormat.parse("2022-09-01"))
                .end(dateFormat.parse("2022-09-15"))
                .value(100)
                .build();
        price5 = Price.builder()
                .id(1)
                .productCode("100")
                .number(1)
                .depart(1)
                .begin(dateFormat.parse("2022-08-10"))
                .end(dateFormat.parse("2022-08-15"))
                .value(100)
                .build();
        price6 = Price.builder()
                .id(1)
                .productCode("100")
                .number(1)
                .depart(1)
                .begin(dateFormat.parse("2022-08-10"))
                .end(dateFormat.parse("2022-08-15"))
                .value(200)
                .build();
        existingPrices = Set.of(price1, price2);
        newPricesForNoPriceCase = Set.of(price3);
        newPricesForNoOverlapCase = Set.of(price4);
        newPricesForOverlapSamePricesCase = Set.of(price5);
        newPricesForOverlapDifferentPricesCase = Set.of(price6);
    }

    @Test
    public void mergeForNoPriceCaseTest() {
        Set<Price> resultPrices = merger.merge(existingPrices, newPricesForNoPriceCase);
        Assert.assertTrue(resultPrices.contains(newPricesForNoPriceCase.stream().findFirst().orElse(null)));
    }

    @Test
    public void mergeForNoOverlapCaseTest() {
        Set<Price> resultPrices = merger.merge(existingPrices, newPricesForNoOverlapCase);
        Assert.assertTrue(resultPrices.contains(newPricesForNoOverlapCase.stream().findFirst().orElse(null)));
    }

    @Test
    public void mergeForOverlapSamePricesCaseTest() {
        Set<Price> resultPrices = merger.merge(existingPrices, newPricesForOverlapSamePricesCase);
        Price newPrice = newPricesForOverlapSamePricesCase.stream().findFirst().orElse(null);
        Price modifiedExistingPrice = existingPrices.stream().filter(price -> {
            assert newPrice != null;
            return price.getProductCode().equals(newPrice.getProductCode());
        }).findFirst().orElse(null);
        assert newPrice != null;
        assert modifiedExistingPrice != null;
        Assert.assertTrue(newPrice.getBegin().equals(modifiedExistingPrice.getBegin()) && newPrice.getEnd().equals(modifiedExistingPrice.getEnd()));
    }

    @Test
    public void mergeForOverlapDifferentPricesCaseTest() {
        Set<Price> resultPrices = merger.merge(existingPrices, newPricesForOverlapDifferentPricesCase);
        Price newPrice = newPricesForOverlapDifferentPricesCase.stream().findFirst().orElse(null);
        Price modifiedExistingPrice = existingPrices.stream().filter(price -> {
            assert newPrice != null;
            return price.getProductCode().equals(newPrice.getProductCode());
        }).findFirst().orElse(null);
        assert newPrice != null;
        assert modifiedExistingPrice != null;
        Assert.assertTrue(newPrice.getBegin().equals(modifiedExistingPrice.getEnd()) && newPrice.getNumber() == modifiedExistingPrice.getNumber() + 1);
    }


}