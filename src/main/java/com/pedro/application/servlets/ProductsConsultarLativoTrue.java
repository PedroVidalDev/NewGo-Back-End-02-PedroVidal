package com.pedro.application.servlets;

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
import java.util.ArrayList;

public class ProductsConsultarLativoTrue extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        PrintWriter writer = resp.getWriter();
        BufferedReader data = req.getReader();

        JsonParser parser = new JsonParser();
        JsonElement tree = parser.parse(data);
        JsonObject array = tree.getAsJsonObject();

        ArrayList<Product> arrayRes = productService.filtrarProdutosPorLativo(array);

        writer.println("=-=-=-=-=-=-=-=-PRODUTOS ATIVOS=-=-=-=-=-=-=-=-");
        for (Product arrayRe : arrayRes) {
            writer.println("Nome: " + arrayRe.getNome());
            writer.println("Descricao: " + arrayRe.getDescricao());
            writer.println("Ean13: " + arrayRe.getEan13());
            writer.println("------------------------");
        }

        writer.flush();
    }
}
