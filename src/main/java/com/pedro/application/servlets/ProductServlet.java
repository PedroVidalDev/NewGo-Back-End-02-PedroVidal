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

public class ProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        PrintWriter writer = resp.getWriter();

        String[] pathInfo = req.getPathInfo().split("/");
        int id = Integer.parseInt(pathInfo[1]);

        Product product = productService.findProduto(id);

        if(product == null){
            writer.println("Produto nao encontrado");
        } else{
            writer.println("=-=-=-=-= Informacoes do produto =-=-=-=-=-");
            writer.println("Nome: " + product.getNome());
            writer.println("Descricao: " + product.getDescricao());
            writer.println("Codigo de barras: " + product.getEan13());
            writer.println("Preco: R$ " + product.getPreco());
            writer.println("Quantidade em estoque: " + product.getQuantidade());
            writer.println("Estoque minimo: " + product.getEstoquemin());
        }
        writer.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        String[] pathInfo = req.getPathInfo().split("/");
        int id = Integer.parseInt(pathInfo[1]);

        PrintWriter writer = resp.getWriter();
        BufferedReader data = req.getReader();

        JsonParser parser = new JsonParser();
        JsonElement tree = parser.parse(data);
        JsonObject array = tree.getAsJsonObject();

        String confirmacao = productService.editarProduto(id, array);

        writer.print(confirmacao);
        writer.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        PrintWriter writer = resp.getWriter();

        String[] pathInfo = req.getPathInfo().split("/");
        int id = Integer.parseInt(pathInfo[1]);

        String confirmacao;
        confirmacao = productService.excluirProduto(id);

        writer.println(confirmacao);
        writer.flush();
    }

}
