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

public class ProductsConsultarLativoTrueOne extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        PrintWriter writer = resp.getWriter();
        BufferedReader data = req.getReader();

        String[] pathInfo = req.getPathInfo().split("/");
        int id = Integer.parseInt(pathInfo[1]);

        Product product = productService.findProduto(id);
        boolean lativo = productService.checkLativoBefore(id);

        // ADICIONAR CONDICIONAL DO PRODUTO EXISTIR AQUI (POR ALGUM MOTIVO ESSA CONDICIONA TA DANDO ERRADO)
        if(lativo == false || product == null){
            writer.println("Produto inativo ou nao encontrado...");
        }
        else {
            writer.println("=-=-=-=-=-=-=-PRODUTO=-=-=-=-=-=-=-=-");
            writer.println("Nome: " + product.getNome());
            writer.println("Descricao: " + product.getDescricao());
            writer.println("Ean13: " + product.getEan13());
            writer.println("------------------------");
        }
        writer.flush();
    }
}
