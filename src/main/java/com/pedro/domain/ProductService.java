package com.pedro.domain;

import com.google.gson.JsonObject;
import com.pedro.infrastructure.DAOs.ProductDAO;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductService {

    // FUNCOES RELACIONADAS AO CRUD //
    public String criarProduto(JsonObject produto) {
        ProductDAO ProductCRUD = new ProductDAO();

        String nome;
        String descricao;
        String ean13;
        float preco;
        int quantidade, min_quantidade;

        try {
            descricao = produto.get("descricao").getAsString();
            ean13 = produto.get("ean13").getAsString();

            if (produto.get("preco").isJsonNull()) {
                preco = 0;
            } else {
                preco = produto.get("preco").getAsFloat();
            }

            if (produto.get("quantidade").isJsonNull()) {
                quantidade = 0;
            } else {
                quantidade = produto.get("quantidade").getAsInt();
            }

            if (produto.get("min_quantidade").isJsonNull()) {
                min_quantidade = 0;
            } else {
                min_quantidade = produto.get("min_quantidade").getAsInt();
            }
        } catch (NullPointerException e) {
            return "Carencia de dados detectada ao criar produto...";
        }

        if (preco < 0) {
            return "Preco nao deve ser menor que zero.";
        }

        else if(quantidade < 0){
            return "Quantidade nao deve ser menor que zero.";
        }

        else if(min_quantidade < 0){
            return "Estoque minimo nao deve ser menor que zero.";
        }

        if (produto.get("nome").isJsonNull()) {
            return "O parametro nome esta nulo. Erro.";
        } else {
            nome = produto.get("nome").getAsString();
            if (nome.isEmpty()) {
                return "O parametro nome esta vazio. Erro.";
            }
        }

        if (ProductCRUD.validate(nome, ean13) == true) {
            return "Ja existe um produto com esse nome cadastrado no sistema.";
        }

        ProductCRUD.create(nome, descricao, ean13, preco, quantidade, min_quantidade);
        return "Produto cadastrado!";
    }

    public ArrayList findProduto(JsonObject info) {
        ProductDAO ProductCRUD = new ProductDAO();

        int id;

        try {
            id = info.get("id").getAsInt();
        } catch (NullPointerException e) {
            return null;
        }

        return ProductCRUD.consultar(id);
    }

    public String editarProduto(JsonObject info) {
        int id;
        float preco;
        String descricao;
        int quantidade;
        int estoque_min;
        boolean lativo;

        ProductDAO ProductCRUD = new ProductDAO();

        try {
            id = info.get("id").getAsInt();
            preco = info.get("preco").getAsFloat();
            descricao = info.get("descricao").getAsString();
            quantidade = info.get("quantidade").getAsInt();
            estoque_min = info.get("estoque_min").getAsInt();
            lativo = info.get("lativo").getAsBoolean();

        } catch (NullPointerException e) {
            return "Carencia de dados detectada.";
        }

        ArrayList produto = ProductCRUD.consultar(id);
        boolean lativo_antigo = (boolean) produto.get(5);

        if (produto == null) {
            return "Produto nao encontrado.";
        } else {

            if (!lativo_antigo && !lativo) {
                return "Produto inativo";
            }

            if (descricao != "" && preco > 0 && quantidade > 0 && estoque_min > 0) {
                ProductCRUD.alterar(id, descricao, preco, quantidade, estoque_min);
                return "Produto alterado.";
            }

            else {
                return "Informacoes novas invalidas.";
            }
        }
    }

    public boolean excluirProduto(JsonObject info) {
        ProductDAO ProductCRUD = new ProductDAO();
        int id;

        try {
            id = info.get("id").getAsInt();
        } catch (NullPointerException e) {
            return false;
        }

        ArrayList produto = ProductCRUD.consultar(id);

        if (produto == null) {
            return false;
        } else {
            ProductCRUD.deletar(id);
        }
        return true;
    }

    public boolean alterarLativo(JsonObject info){
        ProductDAO ProductCRUD = new ProductDAO();

        int id;
        boolean lativo;

        try {
            id = info.get("id").getAsInt();
            lativo = info.get("lativo").getAsBoolean();
        } catch(NullPointerException e){
            return false;
        }

        ArrayList produto = ProductCRUD.consultar(id);

        if(produto == null){
            return false;
        } else{
            ProductCRUD.LativoAlterar(id, lativo);
            return true;
        }
    }
}
