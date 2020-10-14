package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.dao.MemoryProductDao;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.dao.SqliteProductDao;
import ru.akirakozov.sd.refactoring.util.DbTestUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class ServerTest {
    Server server;

    @AfterEach
    void after() throws Exception {
        if (server != null) {
            server.stop();
            server.join();
            server = null;
        }
    }

    @Test
    void testSqlite() throws Exception {
        DbTestUtil.initTestDb(Collections.emptyList());
        doTest(new SqliteProductDao(DbTestUtil.TEST_DB_URL));
    }

    @Test
    void testMemoryDao() throws Exception {
        doTest(new MemoryProductDao());
    }

    void doTest(@NotNull ProductDao productDao) throws Exception {
        server = Main.runServer(Main.DEFAULT_PORT, productDao);

        Assertions.assertEquals(
                "<html><body>\n" +
                "</body></html>\n",
                doRequest("/get-products"));

        doRequest("/add-product?name=Aaa&price=111");
        doRequest("/add-product?name=Bbb&price=333");
        doRequest("/add-product?name=Ccc&price=222");

        Assertions.assertEquals(
                "<html><body>\n" +
                        "Aaa\t111</br>\n" +
                        "Bbb\t333</br>\n" +
                        "Ccc\t222</br>\n" +
                        "</body></html>\n",
                doRequest("/get-products"));

        Assertions.assertEquals(
                "<html><body>\n" +
                        "<h1>Product with max price: </h1>\n" +
                        "Bbb\t333</br>\n" +
                        "</body></html>\n",
                doRequest("/query?command=max"));
        Assertions.assertEquals(
                "<html><body>\n" +
                        "<h1>Product with min price: </h1>\n" +
                        "Aaa\t111</br>\n" +
                        "</body></html>\n",
                doRequest("/query?command=min"));
        Assertions.assertEquals(
                "<html><body>\n" +
                        "Summary price: \n" +
                        "666\n" +
                        "</body></html>\n",
                doRequest("/query?command=sum"));
        Assertions.assertEquals(
                "<html><body>\n" +
                        "Number of products: \n" +
                        "3\n" +
                        "</body></html>\n",
                doRequest("/query?command=count"));
    }

    @NotNull String doRequest(@NotNull String request) throws IOException {
        URL url = new URL("http://localhost:" + Main.DEFAULT_PORT + request);
        return new String(url.openStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
