package com.pedro.application.DTOs;

import java.util.UUID;

public class ProductBatchQntDTO {
    private UUID hash;
    private float valor;

    public ProductBatchQntDTO(
            UUID hash,
            float valor
    ){
        this.hash = hash;
        this.valor = valor;
    }

    public UUID getHash() {
        return hash;
    }

    public float getValor() {
        return valor;
    }
}
