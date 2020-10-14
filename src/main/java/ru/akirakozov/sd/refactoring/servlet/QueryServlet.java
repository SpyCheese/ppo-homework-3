package ru.akirakozov.sd.refactoring.servlet;

import org.jetbrains.annotations.NotNull;
import ru.akirakozov.sd.refactoring.dao.Product;
import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * @author akirakozov
 */
public class QueryServlet extends ServletBase {
    private final @NotNull ProductDao productDao;

    public QueryServlet(@NotNull ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void processRequest(@NotNull HttpServletRequest request, @NotNull PrintWriter writer) {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            writer.println("<h1>Product with max price: </h1>");
            Product product = productDao.getProductWithMaxPrice();
            if (product != null) {
                writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
            }
        } else if ("min".equals(command)) {
            writer.println("<h1>Product with min price: </h1>");
            Product product = productDao.getProductWithMinPrice();
            if (product != null) {
                writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
            }
        } else if ("sum".equals(command)) {
            writer.println("Summary price: ");
            writer.println(productDao.getSummaryPrice());
        } else if ("count".equals(command)) {
            writer.println("Number of products: ");
            writer.println(productDao.getProductCount());
        } else {
            writer.println("Unknown command: " + command);
        }
    }
}
