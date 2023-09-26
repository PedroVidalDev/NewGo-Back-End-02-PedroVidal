package com.pedro.application.servlets;

import com.google.gson.*;
import com.pedro.domain.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ProductOneServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        PrintWriter writer = resp.getWriter();

        String[] pathInfo = req.getPathInfo().split("/");
        String hash = pathInfo[1];

        JsonObject res = productService.findProduto(hash);

        writer.println(res);

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
