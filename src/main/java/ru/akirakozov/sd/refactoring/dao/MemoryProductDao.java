package ru.akirakozov.sd.refactoring.dao;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MemoryProductDao implements ProductDao {
    private final @NotNull List<Product> products;

    public MemoryProductDao() {
        this(Collections.emptyList());
    }

    public MemoryProductDao(@NotNull List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    @Override
    public void addProduct(@NotNull Product product) {
        products.add(product);
    }

    @Override
    public @NotNull List<Product> getProducts() {
        return products;
    }

    @Override
    public Product getProductWithMaxPrice() {
        return Collections.max(products, Comparator.comparing(Product::getPrice));
    }

    @Override
    public Product getProductWithMinPrice() {
        return Collections.min(products, Comparator.comparing(Product::getPrice));
    }

    @Override
    public long getSummaryPrice() {
        return products.stream().mapToLong(Product::getPrice).sum();
    }

    @Override
    public int getProductCount() {
        return products.size();
    }
}
