package ru.akirakozov.sd.refactoring.servlet;

import org.jetbrains.annotations.NotNull;
import ru.akirakozov.sd.refactoring.dao.Product;
import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends ServletBase {
    private final @NotNull ProductDao productDao;

    public GetProductsServlet(@NotNull ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void processRequest(@NotNull HttpServletRequest request, @NotNull PrintWriter writer) {
        for (Product product : productDao.getProducts()) {
            writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
        }
    }
}
