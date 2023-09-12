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

        String nome;
        String descricao;
        String ean13;
        float preco;
        int quantidade, min_quantidade;

        try {
            descricao = array.get("descricao").getAsString();
            ean13 = array.get("ean13").getAsString();

            if (array.get("preco").isJsonNull()) {
                preco = 0;
            } else {
                preco = array.get("preco").getAsFloat();
            }

            if (array.get("quantidade").isJsonNull()) {
                quantidade = 0;
            } else {
                quantidade = array.get("quantidade").getAsInt();
            }

            if (array.get("min_quantidade").isJsonNull()) {
                min_quantidade = 0;
            } else {
                min_quantidade = array.get("min_quantidade").getAsInt();
            }
        } catch(NullPointerException e){
            System.out.println("Carencia de dados detectada...");
            resp.sendError(505);
            return;
        }

        if(preco < 0 || quantidade < 0 || min_quantidade < 0){
            resp.sendError(505);
            return;
        }

        if(array.get("nome").isJsonNull()){
            resp.sendError(505);
            return;
        } else{
            nome = array.get("nome").getAsString();
            if(nome.isEmpty()){
                resp.sendError(505);
                return;
            }
        }

        if(ProductCRUD.validate(nome, ean13) == true){
            resp.sendError(505);
            return;
        }

        ProductCRUD.create(nome, descricao, ean13, preco, quantidade, min_quantidade);

        getServletContext().getRequestDispatcher("/show.jsp").forward(req, resp);

    }
}
