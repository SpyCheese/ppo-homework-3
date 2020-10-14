package ru.akirakozov.sd.refactoring.dao;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ProductDao {
    void addProduct(@NotNull Product product);

    @NotNull List<Product> getProducts();

    Product getProductWithMaxPrice();

    Product getProductWithMinPrice();

    long getSummaryPrice();

    int getProductCount();
}
