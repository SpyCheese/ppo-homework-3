package ru.akirakozov.sd.refactoring.servlet;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.util.DbTestUtil;
import ru.akirakozov.sd.refactoring.util.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServletDbTest {
    StringWriter writer;
    HttpServletResponse response;

    @BeforeEach
    void before() throws Exception {
        writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(printWriter);
        doThrow(RuntimeException.class).when(response).setContentType(argThat(s -> !s.equals("text/html")));
        doThrow(RuntimeException.class).when(response).setStatus(intThat(i -> i != HttpServletResponse.SC_OK));

        DbTestUtil.initTestDb(List.of(
                new Product("Apple", 10),
                new Product("Banana", 65),
                new Product("Cucumber", 35)
        ));
    }

    @Test
    void testGetProducts() throws Exception {
        new GetProductsServlet().doGet(createRequest(Collections.emptyMap()), response);
        String result = writer.getBuffer().toString();
        Assertions.assertTrue(result.contains("Apple\t10"));
        Assertions.assertTrue(result.contains("Banana\t65"));
        Assertions.assertTrue(result.contains("Cucumber\t35"));
    }

    @Test
    void testAddProducts() throws Exception {
        new AddProductServlet().doGet(createRequest(Map.of(
                "name", "Orange",
                "price",  "33"
        )), response);

        List<Product> result = DbTestUtil.getProductsFromTestDb();
        result.sort(Comparator.comparing(Product::getName));
        Assertions.assertEquals(List.of(
                new Product("Apple", 10),
                new Product("Banana", 65),
                new Product("Cucumber", 35),
                new Product("Orange", 33)
        ), result);
    }

    void testQuery(@NotNull String command, @NotNull String expected) throws IOException {
        new QueryServlet().doGet(createRequest(Map.of("command", command)), response);
        String result = writer.getBuffer().toString();
        Assertions.assertTrue(Pattern.compile(expected, Pattern.DOTALL).matcher(result).find(), "Output: " + result);
    }

    @Test
    void testQueryMax() throws IOException {
        testQuery("max", "Product with max price:.*Banana\t65");
    }

    @Test
    void testQueryMin() throws IOException {
        testQuery("min", "Product with min price:.*Apple\t10");
    }

    @Test
    void testQuerySum() throws IOException {
        testQuery("sum", "Summary price:.*110");
    }

    @Test
    void testQueryCount() throws IOException {
        testQuery("count", "Number of products:.*3");
    }

    @Test
    void testQueryUnknown() throws IOException {
        testQuery("xyz", "Unknown command:.*xyz");
    }

    static @NotNull HttpServletRequest createRequest(@NotNull Map<String, String> params) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        //noinspection SuspiciousMethodCalls
        when(request.getParameter(any())).thenAnswer(invocation -> params.get(invocation.getArgument(0)));
        return request;
    }
}