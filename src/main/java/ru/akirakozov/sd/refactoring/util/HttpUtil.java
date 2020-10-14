package ru.akirakozov.sd.refactoring.util;

import org.jetbrains.annotations.NotNull;
import ru.akirakozov.sd.refactoring.dao.Product;

import java.io.PrintWriter;

public class HttpUtil {
    public static void writeProduct(@NotNull PrintWriter writer, @NotNull Product product) {
        writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
    }

    public static void writeHeader(@NotNull PrintWriter writer, @NotNull String s) {
        writer.println("<h1>" + s + "</h1>");
    }
}
