package com.pedro.domain;

import com.google.gson.JsonObject;
import com.pedro.infrastructure.DAOs.ProductDAO;
import com.pedro.infrastructure.entities.Product;

import java.sql.SQLException;
import java.util.ArrayList;

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

        Product product = new Product(nome, descricao, ean13, preco, quantidade, min_quantidade);

        ProductCRUD.create(product);
        return "Produto cadastrado!";
    }

    public Product findProduto(int id) {
        ProductDAO ProductCRUD = new ProductDAO();

        return ProductCRUD.consultar(id);
    }

    public String editarProduto(int id, JsonObject info) {
        float preco;
        String descricao;
        int quantidade;
        int estoque_min;
        boolean lativo;

        ProductDAO ProductCRUD = new ProductDAO();

        try {
            preco = info.get("preco").getAsFloat();
            descricao = info.get("descricao").getAsString();
            quantidade = info.get("quantidade").getAsInt();
            estoque_min = info.get("estoque_min").getAsInt();
            lativo = info.get("lativo").getAsBoolean();

        } catch (NullPointerException e) {
            return "Carencia de dados detectada.";
        }

        Product product_old = ProductCRUD.consultar(id);
        Product product = new Product(product_old.getNome(), descricao, product_old.getEan13(), preco, quantidade, estoque_min);

        boolean lativo_antigo = checkLativoBefore(id);

        if (product_old == null) {
            return "Produto nao encontrado.";
        }

        else if(product_old == product){
            return "Nenhuma alteracao foi feita no produto.";
        }

        else {

            if (!lativo_antigo && !lativo) {
                return "Produto inativo, impossivel atualizar..";
            }

            if (descricao != "" && preco > 0 && quantidade > 0 && estoque_min > 0) {
                ProductCRUD.alterar(id, product);
                return "Produto alterado.";
            }

            else {
                return "Informacoes novas invalidas.";
            }
        }
    }

    public String excluirProduto(int id) {
        ProductDAO ProductCRUD = new ProductDAO();

        Product produto = ProductCRUD.consultar(id);

        if (produto == null) {
            return "Produto nao encontrado.";
        } else {
            ProductCRUD.deletar(id);
        }
        return "Produto deletado.";
    }

    public String alterarLativo(int id, JsonObject info){
        ProductDAO ProductCRUD = new ProductDAO();

        boolean lativo;

        try {
            lativo = info.get("lativo").getAsBoolean();
        } catch(NullPointerException e){
            return "Carencia de dados";
        }

        Product produto = ProductCRUD.consultar(id);

        if(produto == null){
            return "Produto nao encontrado.";
        } else{
            ProductCRUD.LativoAlterar(id, lativo);
            return "Produto teve l_ativo alterado.";
        }
    }

    public boolean checkLativoBefore(int id){
        ProductDAO ProductCRUD = new ProductDAO();

        boolean lativo_antigo = ProductCRUD.consultarLativoAntigo(id);
        return lativo_antigo;
    }

    public ArrayList filtrarProdutosPorLativo(boolean lativo){
        ProductDAO ProductCRUD = new ProductDAO();

        return ProductCRUD.filtrarProdutosLativo(lativo);
    }

    public ArrayList filtrarProdutosComQntAbaixo(){
        ProductDAO ProductCRUD = new ProductDAO();

        return ProductCRUD.filtrarProdutosQntMenorMin();
    }
}
