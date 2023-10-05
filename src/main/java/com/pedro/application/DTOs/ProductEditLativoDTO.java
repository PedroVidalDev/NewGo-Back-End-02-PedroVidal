package com.pedro.application.DTOs;

import java.util.UUID;

public class ProductEditLativoDTO {
    private UUID hash;
    private boolean lativo;

    public ProductEditLativoDTO(
            UUID hash,
            boolean lativo
    ){
        this.hash = hash;
        this.lativo = lativo;
    }

    public UUID getHash() {
        return hash;
    }

    public boolean getLativo() {
        return lativo;
    }
}
