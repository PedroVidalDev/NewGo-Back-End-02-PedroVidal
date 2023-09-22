package com.pedro.application.servlets;

import com.pedro.domain.ProductService;
import com.pedro.infrastructure.entities.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ProductsConsultarQntMenorMin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();

        PrintWriter writer = resp.getWriter();

        ArrayList<Product> arrayRes = productService.filtrarProdutosComQntAbaixo();

        writer.println("=-=-=-=-=-Produtos com qnt. abaixo do estoque minimo=-=-=-=-=-=-=");

        for(Product product : arrayRes){
            writer.println("Nome: " + product.getNome());
            writer.println("Descricao: " + product.getDescricao());
            writer.println("Ean13: " + product.getEan13());
            writer.println("Quantidade: " + product.getQuantidade());
            writer.println("Estoque minimo: " + product.getEstoquemin());
            writer.println("------------------------");
        }

        writer.flush();
    }
}