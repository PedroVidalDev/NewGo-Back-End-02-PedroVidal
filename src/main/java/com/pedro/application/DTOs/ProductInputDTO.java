package com.pedro.application.DTOs;

public class ProductInputDTO {
    private String nome;
    private String descricao;
    private String ean13;
    private float preco;
    private int quantidade;
    private int estoque_min;

    public ProductInputDTO(
            String nome,
            String descricao,
            String ean13,
            float preco,
            int quantidade,
            int estoque_min
    ){
        this.nome = nome;
        this.descricao = descricao;
        this.ean13 = ean13;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoque_min = estoque_min;
    }

    public String getNome(){return nome;}
    public String getDescricao(){return descricao;}
    public String getEan13(){return ean13;}
    public float getPreco(){return preco;}
    public int getQuantidade(){return quantidade;}
    public int getEstoquemin(){return estoque_min;}
}
