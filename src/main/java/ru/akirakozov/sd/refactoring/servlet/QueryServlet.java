package ru.akirakozov.sd.refactoring.servlet;

import org.jetbrains.annotations.NotNull;
import ru.akirakozov.sd.refactoring.dao.Product;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.util.HttpUtil;

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

        switch (command) {
            case "max": {
                HttpUtil.writeHeader(writer, "Product with max price: ");
                Product product = productDao.getProductWithMaxPrice();
                if (product != null) {
                    HttpUtil.writeProduct(writer, product);
                }
                break;
            }
            case "min": {
                HttpUtil.writeHeader(writer, "Product with min price: ");
                Product product = productDao.getProductWithMinPrice();
                if (product != null) {
                    HttpUtil.writeProduct(writer, product);
                }
                break;
            }
            case "sum":
                writer.println("Summary price: ");
                writer.println(productDao.getSummaryPrice());
                break;
            case "count":
                writer.println("Number of products: ");
                writer.println(productDao.getProductCount());
                break;
            default:
                writer.println("Unknown command: " + command);
                break;
        }
    }
}
