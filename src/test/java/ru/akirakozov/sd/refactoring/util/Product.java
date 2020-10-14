package ru.akirakozov.sd.refactoring.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Product {
    @NotNull
    private final String name;
    private final int price;

    public Product(@NotNull String name, int price) {
        this.name = name;
        this.price = price;
    }

    public @NotNull String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price && name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
