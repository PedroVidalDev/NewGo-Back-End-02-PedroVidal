package com.pedro.domain;

import com.google.gson.*;
import com.pedro.application.DTOs.*;
import com.pedro.infrastructure.DAOs.ProductDAO;
import com.pedro.infrastructure.entities.Product;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

public class ProductService {

    public JsonObject criarProduto(JsonObject produto) {
        ProductDAO ProductCRUD = new ProductDAO();
        ProductMapper productMapper = new ProductMapper();

        ResourceBundle messages = ResourceBundle.getBundle("messages");
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
            array.addProperty("mensagem", messages.getString("error.invalidBody"));
            return array;
        }

        if (preco < 0) {
            array.addProperty("mensagem", messages.getString("error.negativePrice"));
            return array;
        }

        else if(quantidade < 0){
            array.addProperty("mensagem", messages.getString("error.negativeQnt"));
            return array;
        }

        else if(min_quantidade < 0){
            array.addProperty("mensagem", messages.getString("error.negativeMinQnt"));
            return array;
        }

        if (produto.get("nome").isJsonNull()) {
            array.addProperty("mensagem", messages.getString("error.nullName"));
            return array;

        } else {
            nome = produto.get("nome").getAsString();
            if (nome.isEmpty()) {
                array.addProperty("mensagem", messages.getString("error.emptyName"));
                return array;
            }
        }

        if (ProductCRUD.validate(nome, ean13)) {
            array.addProperty("mensagem", messages.getString("error.duplicateProduct"));
            return array;
        }

        ProductInputDTO productInput = new ProductInputDTO(nome, descricao, ean13, preco, quantidade, min_quantidade);

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

        ProductOutputDTO productOutput = productMapper.productToOutput(ProductCRUD.create(product));

        array = parseJsonObject(gson.toJson(productOutput));

        return array;

    }

    public JsonObject findProduto(String hash) {
        Gson gson = new Gson();
        ResourceBundle messages = ResourceBundle.getBundle("messages");

        ProductDAO ProductCRUD = new ProductDAO();
        ProductMapper productMapper = new ProductMapper();
        JsonObject res = new JsonObject();

        Product product;

        try {
            product = ProductCRUD.findOneByHash(UUID.fromString(hash));
        } catch(Exception e){
            res.addProperty("mensagem", messages.getString("error.invalidHash"));
            return res;
        }

        if(product == null){
            res.addProperty("mensagem", messages.getString("error.notFoundProduct"));
        } else{
            ProductOutputDTO productOutput = productMapper.productToOutput(product);

            res = parseJsonObject(gson.toJson(productOutput));
        }

        return res;
    }

    public JsonObject editarProduto(String hash, JsonObject info) {
        ResourceBundle messages = ResourceBundle.getBundle("messages");

        float preco;
        String descricao;
        int quantidade;
        int estoque_min;
        boolean lativo;

        ProductDAO ProductCRUD = new ProductDAO();
        ProductMapper productMapper = new ProductMapper();

        Gson gson = new Gson();

        JsonObject res = new JsonObject();

        try {
            preco = info.get("preco").getAsFloat();
            descricao = info.get("descricao").getAsString();
            quantidade = info.get("quantidade").getAsInt();
            estoque_min = info.get("estoque_min").getAsInt();
            lativo = info.get("lativo").getAsBoolean();

        } catch (NullPointerException e) {
            res.addProperty("mensagem", messages.getString("error.invalidBody"));
            return res;
        }

        Product product_old = ProductCRUD.findOneByHash(UUID.fromString(hash));

        if (product_old == null) {
            res.addProperty("mensagem", messages.getString("error.notFoundProduct"));
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
            res.addProperty("mensagem", messages.getString("error.updateDontExist"));
            return res;
        }

        else {

            if (!lativo_antigo && !lativo) {
                res.addProperty("mensagem", messages.getString("error.productInactive"));
                return res;
            }

            else if (product.getDescricao().isEmpty()) {
                res.addProperty("mensagem", messages.getString("error.emptyDesc"));
                return res;
            }

            else if(product.getPreco() < 0){
                res.addProperty("mensagem", messages.getString("error.negativePrice"));
                return res;
            }

            else if(product.getQuantidade() < 0){
                res.addProperty("mensagem", messages.getString("error.negativeQnt"));
                return res;
            }

            else if(product.getEstoquemin() < 0){
                res.addProperty("mensagem", messages.getString("error.negativeMinQnt"));
                return res;
            }

            ProductCRUD.alterar(UUID.fromString(hash), product);
            ProductOutputDTO finalProduct = productMapper.productToOutput(ProductCRUD.findOneByHash(UUID.fromString(hash)));

            res = parseJsonObject(gson.toJson(finalProduct));
            return res;
        }
    }

    public JsonObject excluirProduto(String hash) {
        ProductDAO ProductCRUD = new ProductDAO();
        ProductMapper productMapper = new ProductMapper();

        ResourceBundle messages = ResourceBundle.getBundle("messages");

        JsonObject res = new JsonObject();

        Product product = ProductCRUD.findOneByHash(UUID.fromString(hash));

        ProductOutputDTO productOutput = productMapper.productToOutput(product);

        if (productOutput == null) {
            res.addProperty("mensagem", messages.getString("error.notFoundProduct"));
            return res;
        } else {
            ProductCRUD.deleteByHash(hash);
        }
        res.addProperty("mensagem", messages.getString("product.deleteSuccess"));
        return res;
    }

    public JsonObject alterarLativo(String hash, JsonObject info){
        ProductDAO ProductCRUD = new ProductDAO();
        ProductEditLativoDTO productEditLativoDTO;

        ResourceBundle messages = ResourceBundle.getBundle("messages");
        Gson gson = new Gson();

        boolean lativo;

        try {
            lativo = info.get("lativo").getAsBoolean();

        } catch(NullPointerException e){
            JsonObject resErro = new JsonObject();
            resErro.addProperty("mensagem", messages.getString("error.invalidBody"));
            return resErro;
        }

        productEditLativoDTO = new ProductEditLativoDTO(UUID.fromString(hash), lativo);
        JsonObject res = parseJsonObject(gson.toJson(productEditLativoDTO));

        Product product = ProductCRUD.findOneByHash(productEditLativoDTO.getHash());

        if(product == null){
            res.addProperty("mensagem", messages.getString("error.notFoundProduct"));
            return res;
        } else{
            ProductCRUD.LativoAlterar(productEditLativoDTO.getHash(), productEditLativoDTO.getLativo());

            res.addProperty("mensagem", messages.getString("product.updateSuccess"));

            return res;
        }
    }

    public boolean checkLativoBefore(String hash){
        ProductDAO ProductCRUD = new ProductDAO();

        boolean lativo_antigo = ProductCRUD.consultarLativoAntigo(UUID.fromString(hash));
        return lativo_antigo;
    }

    public JsonObject findProdutoLativoTrue(String hash){
        JsonObject product = findProduto(hash);
        ResourceBundle messages = ResourceBundle.getBundle("messages");

        if(product.get("lativo").getAsBoolean()){
            return product;
        } else{
            JsonObject resErro = new JsonObject();
            resErro.addProperty("hash", hash);
            resErro.addProperty("aviso", messages.getString("error.productInactive"));
            return resErro;
        }
    }

    public JsonArray filtrarProdutosPorLativo(String lativo){
        ProductDAO ProductCRUD = new ProductDAO();
        ProductMapper productMapper = new ProductMapper();

        Gson gson = new Gson();

        JsonArray res = new JsonArray();

        boolean lativoBool;
        lativoBool = Boolean.parseBoolean(lativo);

        ArrayList<Product> listProducts = ProductCRUD.findAllByLativo(lativoBool);

        ArrayList<ProductOutputDTO> listDTO = new ArrayList<ProductOutputDTO>();

        for (Product product : listProducts){
            listDTO.add(productMapper.productToOutput(product));
        }

        res = parseJsonArray(gson.toJson(listDTO));

        return res;
    }

    public JsonArray filtrarProdutosComQntAbaixo(){
        ProductDAO ProductCRUD = new ProductDAO();
        ProductMapper productMapper = new ProductMapper();

        Gson gson = new Gson();

        ArrayList<Product> listProducts = ProductCRUD.findAllByQntLowerMin();
        ArrayList<ProductOutputDTO> listDTO = new ArrayList<ProductOutputDTO>();

        for (Product product : listProducts){
            listDTO.add(productMapper.productToOutput(product));
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
        ProductMapper productMapper = new ProductMapper();

        ResourceBundle messages = ResourceBundle.getBundle("messages");
        Gson gson = new Gson();

        JsonArray resArray = new JsonArray();
        JsonObject selectedProduct = new JsonObject();

        float finalValue;

        for (JsonElement element : array){

            JsonObject elementObject = element.getAsJsonObject();

            ProductBatchPriceDTO productBatchPriceDTO;

            try {

                productBatchPriceDTO = new ProductBatchPriceDTO(
                        UUID.fromString(elementObject.get("hash").getAsString()),
                        elementObject.get("operacao").getAsString(),
                        elementObject.get("valor").getAsFloat()
                );

                selectedProduct = findProduto(productBatchPriceDTO.getHash().toString());

                try{
                    JsonObject resErro = element.getAsJsonObject();
                    resErro.addProperty("aviso", selectedProduct.get("mensagem").getAsString());
                    resArray.add(resErro);

                } catch(NullPointerException e){

                    if(selectedProduct.get("lativo").getAsBoolean()){

                        if(productBatchPriceDTO.getOperacao().equals("valor-fixo")){
                            finalValue = productBatchPriceDTO.getValor();

                            JsonObject res = parseJsonObject(gson.toJson(productBatchPriceDTO));

                            if(finalValue < 0){
                                res.addProperty("aviso", messages.getString("error.negativePrice"));
                                resArray.add(res);
                            } else {

                                ProductCRUD.editPriceBatch(productBatchPriceDTO.getHash(), finalValue);

                                res.addProperty("aviso", messages.getString("product.updateSuccess"));

                                resArray.add(res);
                            }
                        }

                        else if(productBatchPriceDTO.getOperacao().equals("porcentagem")){

                            finalValue = selectedProduct.get("preco").getAsFloat() + (selectedProduct.get("preco").getAsFloat() * (productBatchPriceDTO.getValor() / 100));

                            JsonObject res = parseJsonObject(gson.toJson(productBatchPriceDTO));

                            if(finalValue < 0){
                                res.addProperty("aviso", messages.getString("error.negativePrice"));
                                resArray.add(res);
                            } else{

                                ProductCRUD.editPriceBatch(productBatchPriceDTO.getHash(), finalValue);

                                res.addProperty("aviso", messages.getString("product.updateSuccess"));

                                resArray.add(res);
                            }

                        }

                        else if(productBatchPriceDTO.getOperacao().equals("valor")){
                            finalValue = selectedProduct.get("preco").getAsFloat() + productBatchPriceDTO.getValor();

                            JsonObject res = parseJsonObject(gson.toJson(productBatchPriceDTO));

                            if(finalValue < 0){
                                res.addProperty("aviso", messages.getString("error.negativePrice"));
                                resArray.add(res);
                            } else{
                                ProductCRUD.editPriceBatch(productBatchPriceDTO.getHash(), finalValue);

                                res.addProperty("hash", messages.getString("product.updateSuccess"));

                                resArray.add(res);
                            }
                        }

                        else{
                            JsonObject resErro = element.getAsJsonObject();
                            resErro.addProperty("aviso", messages.getString("error.invalidOperation"));
                            resArray.add(resErro);
                        }

                    } else{
                        JsonObject resErro = element.getAsJsonObject();
                        resErro.addProperty("aviso", messages.getString("error.productInactive"));
                        resArray.add(resErro);
                    }

                }

            } catch(NullPointerException e){
                JsonObject resErro = element.getAsJsonObject();
                resErro.addProperty("aviso", messages.getString("error.invalidBody"));
                resArray.add(resErro);
            }

        }

        return resArray;
    }

    public JsonArray editarQntLote(JsonArray array){
        ProductDAO ProductCRUD = new ProductDAO();
        ProductMapper productMapper = new ProductMapper();

        ResourceBundle messages = ResourceBundle.getBundle("messages");
        Gson gson = new Gson();

        JsonArray resArray = new JsonArray();
        JsonObject selectedProduct = new JsonObject();

        for (JsonElement element : array){
            JsonObject elementObject = element.getAsJsonObject();

            ProductBatchQntDTO productBatchQntDTO;

            try {
                productBatchQntDTO = new ProductBatchQntDTO(
                        UUID.fromString(elementObject.get("hash").getAsString()),
                        elementObject.get("valor").getAsFloat()
                );

                selectedProduct = findProduto(productBatchQntDTO.getHash().toString());

                JsonObject res = parseJsonObject(gson.toJson(productBatchQntDTO));

                try{
                    res.addProperty("aviso", selectedProduct.get("mensagem").getAsString());
                    resArray.add(res);

                } catch(NullPointerException e) {

                    if (selectedProduct.get("lativo").getAsBoolean()) {

                        Float valorFinal = selectedProduct.get("quantidade").getAsFloat() + productBatchQntDTO.getValor();

                        if(valorFinal < 0) {
                            res.addProperty("aviso", messages.getString("error.negativePrice"));
                            resArray.add(res);

                        } else{
                            ProductCRUD.editQntBatch(productBatchQntDTO.getHash(), valorFinal);

                            res.addProperty("aviso", messages.getString("product.updateSuccess"));

                            resArray.add(res);
                        }

                    } else {
                        res.addProperty("aviso", messages.getString("error.productInactive"));
                        resArray.add(res);
                    }
                }
            } catch(NullPointerException e){
                JsonObject resErro = element.getAsJsonObject();
                resErro.addProperty("aviso", messages.getString("error.invalidBody"));
                resArray.add(resErro);
            }
        }
        return resArray;
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
