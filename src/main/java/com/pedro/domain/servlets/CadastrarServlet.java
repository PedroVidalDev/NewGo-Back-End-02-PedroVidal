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

public class CadastrarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDAO ProductCRUD = new ProductDAO();

        BufferedReader data = req.getReader();

        JsonParser parser = new JsonParser();
        JsonElement tree = parser.parse(data);
        JsonObject array = tree.getAsJsonObject();

        String nome = array.get("nome").getAsString();
        String descricao = array.get("descricao").getAsString();
        String ean13 = array.get("ean13").getAsString();
        float preco = array.get("preco").getAsFloat();
        int quantidade = array.get("quantidade").getAsInt();
        int min_quantidade = array.get("min_quantidade").getAsInt();

        if(preco < 0 || quantidade < 0 || min_quantidade < 0){
            resp.sendError(505);
            return;
        }

        else if(nome.equals("") || nome == null){
            resp.sendError(505);
            return;
        }

        else if(ProductCRUD.validate(nome, ean13) == true){
            resp.sendError(505);
            return;
        }

        ProductCRUD.create(nome, descricao, ean13, preco, quantidade, min_quantidade);

        getServletContext().getRequestDispatcher("/show.jsp").forward(req, resp);

    }
}
