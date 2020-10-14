package ru.akirakozov.sd.refactoring.servlet;

import org.jetbrains.annotations.NotNull;
import ru.akirakozov.sd.refactoring.dao.Product;
import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * @author akirakozov
 */
public class AddProductServlet extends ServletBase {
    private final @NotNull ProductDao productDao;

    public AddProductServlet(@NotNull ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void processRequest(@NotNull HttpServletRequest request, @NotNull PrintWriter writer) {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));
        productDao.addProduct(new Product(name, price));
        writer.println("OK");
    }
}
