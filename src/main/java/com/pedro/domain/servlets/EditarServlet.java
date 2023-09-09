package com.pedro.domain.servlets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pedro.infrastructure.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class EditarServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDAO ProductCRUD = new ProductDAO();

        BufferedReader data = req.getReader();

        JsonParser parser = new JsonParser();
        JsonElement tree = parser.parse(data);
        JsonObject array = tree.getAsJsonObject();

        int id = array.get("id").getAsInt();
        String descricao = array.get("descricao").getAsString();
        int quantidade = array.get("quantidade").getAsInt();
        int estoque_min = array.get("estoque_min").getAsInt();

        ArrayList produto = ProductCRUD.consultar(id);

        if(produto == null){
            System.out.println("Produto nao encontrado");
        } else{

            if(descricao != "" && quantidade > 0 && estoque_min > 0){
                ProductCRUD.alterar(id, descricao, quantidade, estoque_min);
                System.out.println("Produto alterado.");
            } else{
                System.out.println("Informacoes novas invalidas.");
            }

        }
    }
}
