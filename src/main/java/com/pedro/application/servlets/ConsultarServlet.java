package com.pedro.application.servlets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pedro.domain.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConsultarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        BufferedReader data = req.getReader();

        JsonParser parser = new JsonParser();
        JsonElement tree = parser.parse(data);
        JsonObject array = tree.getAsJsonObject();

        ArrayList produto = productService.findProduto(array);

        if(produto == null){
            System.out.println("Produto nao encontrado");
        } else{
            System.out.println("=-=-=-=-= Informacoes do produto =-=-=-=-=-");
            System.out.println("Nome: " + produto.get(0));
            System.out.println("Descricao: " + produto.get(1));
            System.out.println("Codigo de barras: " + produto.get(2));
            System.out.println("Preco: R$ " + produto.get(3));
            System.out.println("Quantidade em estoque: " + produto.get(4));
        }
    }
}
