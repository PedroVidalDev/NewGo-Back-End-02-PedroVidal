package com.pedro.application.servlets;

import com.google.gson.JsonArray;
import com.pedro.domain.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ProductsConsultarLativo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        PrintWriter writer = resp.getWriter();

        String lativo = req.getParameter("lativo");

        JsonArray res = productService.filtrarProdutosPorLativo(lativo);

        writer.println(res);

        writer.flush();
    }
}
