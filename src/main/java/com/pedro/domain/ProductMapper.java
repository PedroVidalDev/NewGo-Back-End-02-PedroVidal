package com.pedro.domain;

import com.pedro.application.DTOs.ProductOutputDTO;
import com.pedro.infrastructure.entities.Product;

public class ProductMapper {
    public ProductOutputDTO productToOutput(Product product){
        ProductOutputDTO productOutput = new ProductOutputDTO(
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
