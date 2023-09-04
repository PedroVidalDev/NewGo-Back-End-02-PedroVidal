package com.pedro.domain.servlets;

import com.pedro.infrastructure.createProduct;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CadastrarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nome = req.getParameter("nome");
        String descricao = req.getParameter("descricao");
        String preco = req.getParameter("preco");
        String quantidade = req.getParameter("quantidade");
        String min_quantidade = req.getParameter("min_quantidade");

        String ean13 = "teste";

        createProduct crProduct = new createProduct();
        crProduct.create(nome, descricao, ean13, preco, quantidade, min_quantidade);

        getServletContext().getRequestDispatcher("/show.jsp").forward(req, resp);

    }
}
