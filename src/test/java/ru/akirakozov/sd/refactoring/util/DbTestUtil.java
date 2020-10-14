package ru.akirakozov.sd.refactoring.util;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbTestUtil {
    public static void initTestDb(@NotNull List<Product> products) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            String sql =
                    "DROP TABLE IF EXISTS PRODUCT;" +
                    "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "NAME TEXT NOT NULL," +
                    "PRICE INT NOT NULL);";
            stmt.executeUpdate(sql);
            stmt.close();
            for (Product product : products) {
                PreparedStatement prep = c.prepareStatement("INSERT INTO PRODUCT (NAME, PRICE) VALUES (?, ?);");
                prep.setString(1, product.getName());
                prep.setInt(2, product.getPrice());
                prep.executeUpdate();
                prep.close();
            }
        }
    }

    public static @NotNull List<Product> getProductsFromTestDb() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            String sql = "SELECT NAME, PRICE FROM PRODUCT;";
            ResultSet rs = stmt.executeQuery(sql);
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(new Product(rs.getString(1), rs.getInt(2)));
            }
            rs.close();
            stmt.close();
            return products;
        }
    }
}
