package com.pedro.domain;

import com.pedro.application.DTOs.ProductOutput;
import com.pedro.infrastructure.entities.Product;

public class ProductMapper {
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
}
