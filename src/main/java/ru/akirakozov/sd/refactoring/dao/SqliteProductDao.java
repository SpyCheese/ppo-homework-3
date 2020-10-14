package ru.akirakozov.sd.refactoring.dao;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteProductDao implements ProductDao {
    private final @NotNull String url;

    public SqliteProductDao(@NotNull String url) {
        this.url = url;
        withConnection(c -> {
            String sql =
                    "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME TEXT NOT NULL," +
                    " PRICE INT NOT NULL)";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            return null;
        });
    }

    @Override
    public void addProduct(@NotNull Product product) {
        withConnection(c -> {
            PreparedStatement prep = c.prepareStatement("INSERT INTO PRODUCT (NAME, PRICE) VALUES (?, ?);");
            prep.setString(1, product.getName());
            prep.setLong(2, product.getPrice());
            prep.executeUpdate();
            return null;
        });
    }

    @Override
    public @NotNull List<Product> getProducts() {
        return withConnection(c -> {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NAME, PRICE FROM PRODUCT;");
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(resultToProduct(rs));
            }
            rs.close();
            stmt.close();
            return products;
        });
    }

    @Override
    public Product getProductWithMaxPrice() {
        return withConnection(c -> {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NAME, PRICE FROM PRODUCT ORDER BY PRICE DESC LIMIT 1;");
            return rs.next() ? resultToProduct(rs) : null;
        });
    }

    @Override
    public Product getProductWithMinPrice() {
        return withConnection(c -> {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NAME, PRICE FROM PRODUCT ORDER BY PRICE LIMIT 1;");
            return rs.next() ? resultToProduct(rs) : null;
        });
    }

    @Override
    public long getSummaryPrice() {
        return withConnection(c -> {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
            return rs.next() ? rs.getLong(1) : 0;
        });
    }

    @Override
    public int getProductCount() {
        return withConnection(c -> {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(price) FROM PRODUCT");
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private interface ThrowingFunction<T> {
        T apply(@NotNull Connection c) throws SQLException;
    }

    private <T> T withConnection(@NotNull ThrowingFunction<T> f) {
        try (Connection c = DriverManager.getConnection(url)) {
            return f.apply(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull Product resultToProduct(@NotNull ResultSet rs) throws SQLException {
        return new Product(rs.getString(1), rs.getLong(2));
    }
}
