package com.pedro.domain.servlets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pedro.infrastructure.ProductDAO;
import com.sun.org.apache.xpath.internal.operations.Bool;

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

        int esc;

        int id;
        String descricao;
        int quantidade;
        int estoque_min;
        boolean lativo;

        try {
            id = array.get("id").getAsInt();
            descricao = array.get("descricao").getAsString();
            quantidade = array.get("quantidade").getAsInt();
            estoque_min = array.get("estoque_min").getAsInt();
            lativo = array.get("lativo").getAsBoolean();

        } catch(NullPointerException e){
            System.out.println("Carencia de dados ao editar detectada...");
            resp.sendError(505);
            return;
        }

        ArrayList produto = ProductCRUD.consultar(id);
        boolean lativo_antigo = (boolean) produto.get(5);

        if(produto == null){
            System.out.println("Produto nao encontrado");
        }

        else{

            if(!lativo_antigo && !lativo) {
                System.out.println("Produto esta inativo.");
                System.out.println("Nao sera possivel alterar o produto.");
                resp.sendError(505);
                return;
            }

            if(descricao != "" && quantidade > 0 && estoque_min > 0){
                ProductCRUD.alterar(id, descricao, quantidade, estoque_min);
                System.out.println("Produto alterado.");
            }

            else{
                System.out.println("Informacoes novas invalidas.");
            }

        }
    }
}
