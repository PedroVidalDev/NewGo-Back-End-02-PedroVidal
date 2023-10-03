package com.pedro.application.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pedro.domain.ProductService;
import com.pedro.infrastructure.entities.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class ProductsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        PrintWriter writer = resp.getWriter();
        BufferedReader data = req.getReader();

        JsonParser parser = new JsonParser();
        JsonElement tree = parser.parse(data);
        JsonObject array = tree.getAsJsonObject();

        JsonObject res;

        res = productService.criarProduto(array);

        writer.print(res);
        writer.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        PrintWriter writer = resp.getWriter();

        String hash;

        JsonObject res;
        JsonArray resArray;

        try {
            String[] pathInfo = req.getPathInfo().split("/");
            if (pathInfo.length == 2) {

                if (pathInfo[1].equals("consultarQntMenorMin")) {
                    resArray = productService.filtrarProdutosComQntAbaixo();

                    writer.println(resArray);

                } else {
                    hash = pathInfo[1];

                    res = productService.findProduto(hash);

                    writer.println(res);
                }

            }

            else if (pathInfo.length == 3 && pathInfo[2].equals("lativoTrueOne")) {
                hash = pathInfo[1];

                res = productService.findProdutoLativoTrue(hash);

                writer.println(res);
            }

        } catch(NullPointerException e){
            String lativo = req.getParameter("lativo");

            resArray = productService.filtrarProdutosPorLativo(lativo);

            writer.println(resArray);
        }

        writer.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        String[] pathInfo = req.getPathInfo().split("/");
        String hash = pathInfo[1];

        PrintWriter writer = resp.getWriter();
        BufferedReader data = req.getReader();

        JsonParser parser = new JsonParser();
        JsonElement tree = parser.parse(data);
        JsonObject array = tree.getAsJsonObject();

        JsonObject res = productService.editarProduto(hash, array);

        writer.print(res);
        writer.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        PrintWriter writer = resp.getWriter();

        String[] pathInfo = req.getPathInfo().split("/");
        String hash = pathInfo[1];

        JsonObject res = productService.excluirProduto(hash);

        writer.println(res);

        writer.flush();
    }

}
