package com.pedro.application.DTOs;

import java.util.UUID;

public class ProductBatchPriceDTO {
    private UUID hash;
    private String operacao;
    private float valor;

    public ProductBatchPriceDTO(
            UUID hash,
            String operacao,
            float valor
    ){
        this.hash = hash;
        this.operacao = operacao;
        this.valor = valor;
    }

    public UUID getHash() {
        return hash;
    }

    public String getOperacao() {
        return operacao;
    }

    public float getValor() {
        return valor;
    }
}
