package com.pedro.domain;

import com.google.gson.*;
import com.pedro.application.DTOs.ProductInput;
import com.pedro.application.DTOs.ProductOutput;
import com.pedro.infrastructure.DAOs.ProductDAO;
import com.pedro.infrastructure.entities.Product;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

import javax.swing.text.AttributeSet;
import java.util.ArrayList;
import java.util.UUID;

public class ProductService {

    public JsonObject criarProduto(JsonObject produto) {
        ProductDAO ProductCRUD = new ProductDAO();
        Gson gson = new Gson();

        JsonObject array = new JsonObject();

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
            array.addProperty("mensagem", "Carencia de dados detectada ao criar produto...");
            return array;
        }

        if (preco < 0) {
            array.addProperty("mensagem", "Preco nao deve ser menor que zero.");
            return array;
        }

        else if(quantidade < 0){
            array.addProperty("mensagem", "Quantidade nao deve ser menor que zero.");
            return array;
        }

        else if(min_quantidade < 0){
            array.addProperty("mensagem", "Estoque minimo nao deve ser menor que zero.");
            return array;
        }

        if (produto.get("nome").isJsonNull()) {
            array.addProperty("mensagem", "O parametro nome esta nulo. Erro.");
            return array;

        } else {
            nome = produto.get("nome").getAsString();
            if (nome.isEmpty()) {
                array.addProperty("mensagem", "O parametro nome esta vazio. Erro.");
                return array;
            }
        }

        if (ProductCRUD.validate(nome, ean13)) {
            array.addProperty("mensagem", "Ja existe um produto com esse nome cadastrado no sistema.");
            return array;
        }

        ProductInput productInput = new ProductInput(nome, descricao, ean13, preco, quantidade, min_quantidade);

        Product product = new Product(
                0,
                UUID.randomUUID(),
                productInput.getNome(),
                productInput.getDescricao(),
                productInput.getEan13(),
                productInput.getPreco(),
                productInput.getQuantidade(),
                productInput.getEstoquemin(),
                null,
                null,
                false
        );

        ProductOutput productOutput = productToOutput(ProductCRUD.create(product));

        array = parseJsonObject(gson.toJson(productOutput));

        return array;

    }

    public JsonObject findProduto(String hash) {
        Gson gson = new Gson();

        ProductDAO ProductCRUD = new ProductDAO();
        JsonObject res = new JsonObject();

        Product product;

        try {
            product = ProductCRUD.consultar(UUID.fromString(hash));
        } catch(Exception e){
            res.addProperty("mensagem", "Hash invalida.");
            return res;
        }

        if(product == null){
            res.addProperty("mensagem", "Produto nao encontrado.");
        } else{
            ProductOutput productOutput = productToOutput(product);

            res = parseJsonObject(gson.toJson(productOutput));
        }

        return res;
    }

    public JsonObject editarProduto(String hash, JsonObject info) {
        float preco;
        String descricao;
        int quantidade;
        int estoque_min;
        boolean lativo;

        ProductDAO ProductCRUD = new ProductDAO();

        Gson gson = new Gson();

        JsonObject res = new JsonObject();

        try {
            preco = info.get("preco").getAsFloat();
            descricao = info.get("descricao").getAsString();
            quantidade = info.get("quantidade").getAsInt();
            estoque_min = info.get("estoque_min").getAsInt();
            lativo = info.get("lativo").getAsBoolean();

        } catch (NullPointerException e) {
            res.addProperty("mensagem", "Carencia de dados.");
            return res;
        }

        Product product_old = ProductCRUD.consultar(UUID.fromString(hash));

        if (product_old == null) {
            res.addProperty("mensagem", "Produto nao encontrado.");
            return res;
        }

        Product product = new Product(
                product_old.getId(),
                product_old.getHash(),
                product_old.getNome(),
                descricao,
                product_old.getEan13(),
                preco,
                quantidade,
                estoque_min,
                product_old.getDtcreate(),
                product_old.getDtupdate(),
                product_old.getLativo()
        );

        boolean lativo_antigo = checkLativoBefore(hash);

        if(product_old.equals(product)){
            res.addProperty("mensagem", "Nenhuma alteracao foi feita no produto.");
            return res;
        }

        else {

            if (!lativo_antigo && !lativo) {
                res.addProperty("mensagem", "Produto inativo, impossivel atualizar..");
                return res;
            }

            else if (product.getDescricao().isEmpty()) {
                res.addProperty("mensagem", "Descricao nao pode ser vazia.");
                return res;
            }

            else if(product.getPreco() < 0){
                res.addProperty("mensagem", "Preco nao pode ser negativo.");
                return res;
            }

            else if(product.getQuantidade() < 0){
                res.addProperty("mensagem", "Quantidade nao pode ser negativa.");
                return res;
            }

            else if(product.getEstoquemin() < 0){
                res.addProperty("mensagem", "Estoque minimo nao pode ser negativo.");
                return res;
            }

            ProductCRUD.alterar(UUID.fromString(hash), product);
            ProductOutput finalProduct = productToOutput(ProductCRUD.consultar(UUID.fromString(hash)));

            res = parseJsonObject(gson.toJson(finalProduct));
            return res;
        }
    }

    public JsonObject excluirProduto(String hash) {
        ProductDAO ProductCRUD = new ProductDAO();

        JsonObject res = new JsonObject();

        Product product = ProductCRUD.consultar(UUID.fromString(hash));

        ProductOutput productOutput = productToOutput(product);

        if (productOutput == null) {
            res.addProperty("mensagem", "Produto nao encontrado.");
            return res;
        } else {
            ProductCRUD.deletar(hash);
        }
        res.addProperty("mensagem", "Produto deletado.");
        return res;
    }

    public JsonObject alterarLativo(String hash, JsonObject info){
        ProductDAO ProductCRUD = new ProductDAO();

        Gson gson = new Gson();

        JsonObject res = new JsonObject();

        boolean lativo;

        try {
            lativo = info.get("lativo").getAsBoolean();
        } catch(NullPointerException e){
            res.addProperty("mensagem", "Carencia de dados.");
            return res;
        }

        Product produto = ProductCRUD.consultar(UUID.fromString(hash));

        if(produto == null){
            res.addProperty("mensagem", "Produto nao encontrado.");
            return res;
        } else{
            ProductCRUD.LativoAlterar(hash, lativo);

            res = parseJsonObject(gson.toJson(ProductCRUD.consultar(UUID.fromString(hash))));
            return res;
        }
    }

    public boolean checkLativoBefore(String hash){
        ProductDAO ProductCRUD = new ProductDAO();

        boolean lativo_antigo = ProductCRUD.consultarLativoAntigo(UUID.fromString(hash));
        return lativo_antigo;
    }

    public JsonArray filtrarProdutosPorLativo(String lativo){
        ProductDAO ProductCRUD = new ProductDAO();

        Gson gson = new Gson();

        JsonArray res = new JsonArray();

        boolean lativoBool;
        lativoBool = Boolean.parseBoolean(lativo);

        ArrayList<Product> listProducts = ProductCRUD.filtrarProdutosLativo(lativoBool);

        ArrayList<ProductOutput> listDTO = new ArrayList<ProductOutput>();

        for (Product product : listProducts){
            listDTO.add(productToOutput(product));
        }

        res = parseJsonArray(gson.toJson(listDTO));

        return res;
    }

    public JsonArray filtrarProdutosComQntAbaixo(){
        ProductDAO ProductCRUD = new ProductDAO();

        Gson gson = new Gson();

        ArrayList<Product> listProducts = ProductCRUD.filtrarProdutosQntMenorMin();
        ArrayList<ProductOutput> listDTO = new ArrayList<ProductOutput>();

        for (Product product : listProducts){
            listDTO.add(productToOutput(product));
        }

        JsonArray listJson = parseJsonArray(gson.toJson(listDTO));

        return listJson;
    }

    public JsonArray criarProdutosLote(JsonArray array){

        JsonObject res = new JsonObject();
        JsonArray resArray = new JsonArray();

        for (JsonElement element : array) {
            res = criarProduto(element.getAsJsonObject());

            try{
                JsonObject resErro = element.getAsJsonObject();
                resErro.addProperty("aviso", res.get("mensagem").getAsString());
                resArray.add(resErro);
            } catch(NullPointerException e){
                resArray.add(res);
            }

        }
        return resArray;

    }

    public JsonArray editarPrecoLote(JsonArray array){
        ProductDAO ProductCRUD = new ProductDAO();

        JsonArray resArray = new JsonArray();
        JsonObject res = new JsonObject();

        float finalValue;

        for (JsonElement element : array){

            JsonObject elementObject = element.getAsJsonObject();
            Float valor = elementObject.get("valor").getAsFloat();
            String operacao = elementObject.get("operacao").getAsString();
            String hash = elementObject.get("hash").getAsString();

            res = findProduto(hash);

            try{
                JsonObject resErro = element.getAsJsonObject();
                resErro.addProperty("aviso", res.get("mensagem").getAsString());
                resArray.add(resErro);

            } catch(NullPointerException e){

                if(operacao.equals("aumentar")){
                    finalValue = res.get("preco").getAsFloat() + (res.get("preco").getAsFloat() * (valor / 100));

                    ProductCRUD.editPriceBatch(UUID.fromString(hash), finalValue);

                    res = findProduto(hash);

                    resArray.add(res);
                }

                else if(operacao.equals("diminuir")){

                    if(valor > 100){
                        JsonObject resErro = element.getAsJsonObject();
                        resErro.addProperty("aviso", "Valor nao deve ser maior que 100");
                        resArray.add(resErro);
                    } else{
                        finalValue = res.get("preco").getAsFloat() - (res.get("preco").getAsFloat() * (valor / 100));

                        ProductCRUD.editPriceBatch(UUID.fromString(hash), finalValue);

                        res = findProduto(hash);

                        resArray.add(res);
                    }

                }

                else{
                    JsonObject resErro = element.getAsJsonObject();
                    resErro.addProperty("aviso", "Operacao invalida.");
                    resArray.add(resErro);
                }
            }
        }

        return resArray;
    }

    public ProductOutput productToOutput(Product product){
        ProductOutput productOutput = new ProductOutput(
                product.getId(),
                product.getHash(),
                product.getNome(),
                product.getDescricao(),
                product.getEan13(),
                product.getPreco(),
                product.getQuantidade(),
                product.getEstoquemin(),
                product.getDtcreate(),
                product.getDtupdate(),
                product.getLativo()
        );

        return productOutput;
    }

    public JsonObject parseJsonObject(String element){
        JsonParser parser = new JsonParser();

        JsonElement tree = parser.parse(element);
        JsonObject array = tree.getAsJsonObject();

        return array;
    }

    public JsonArray parseJsonArray(String element){
        JsonParser parser = new JsonParser();

        JsonElement tree = parser.parse(element);
        JsonArray array = tree.getAsJsonArray();

        return array;
    }
}
