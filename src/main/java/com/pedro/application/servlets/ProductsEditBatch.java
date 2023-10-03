package com.pedro.application.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pedro.domain.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ProductsEditBatch extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();
        Gson gson = new Gson();

        PrintWriter writer = resp.getWriter();

        JsonArray resArray;

        String[] pathInfo = req.getPathInfo().split("/");

        BufferedReader data = req.getReader();

        JsonParser parser = new JsonParser();
        JsonElement tree = parser.parse(data);
        JsonArray array = tree.getAsJsonArray();

        if(pathInfo[1].equals("price")){
            resArray = productService.editarPrecoLote(array);

            writer.println(gson.toJson(resArray));
        }

        else if(pathInfo[1].equals("qnt")){
            resArray = productService.editarQntLote(array);

            writer.println(gson.toJson(resArray));
        }

        writer.flush();
    }


}
