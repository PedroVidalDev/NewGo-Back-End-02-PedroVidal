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
import java.util.ArrayList;

public class ConsultarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        BufferedReader data = req.getReader();

        JsonParser parser = new JsonParser();
        JsonElement tree = parser.parse(data);
        JsonObject array = tree.getAsJsonObject();

        Product product = productService.findProduto(array);

        if(product == null){
            System.out.println("Produto nao encontrado");
        } else{
            System.out.println("=-=-=-=-= Informacoes do produto =-=-=-=-=-");
            System.out.println("Nome: " + product.getNome());
            System.out.println("Descricao: " + product.getDescricao());
            System.out.println("Codigo de barras: " + product.getEan13());
            System.out.println("Preco: R$ " + product.getPreco());
            System.out.println("Quantidade em estoque: " + product.getQuantidade());
            System.out.println("Estoque minimo: " + product.getEstoquemin());
        }
    }
}
