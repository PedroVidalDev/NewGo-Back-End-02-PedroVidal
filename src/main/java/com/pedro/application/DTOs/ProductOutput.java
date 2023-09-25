package com.pedro.application.DTOs;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public class ProductOutput {
    private int id;
    private UUID hash;
    private String nome;
    private String descricao;
    private String ean13;
    private float preco;
    private int quantidade;
    private int estoque_min;
    private Timestamp dtcreate;
    private Timestamp dtupdate;
    private boolean lativo;

    public ProductOutput(
            int id,
            UUID hash,
            String nome,
            String descricao,
            String ean13,
            float preco,
            int quantidade,
            int estoque_min,
            Timestamp dtcreate,
            Timestamp dtupdate,
            boolean lativo
    ){
        this.id = id;
        this.hash = hash;
        this.nome = nome;
        this.descricao = descricao;
        this.ean13 = ean13;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoque_min = estoque_min;
        this.dtcreate = dtcreate;
        this.dtupdate = dtupdate;
        this.lativo = lativo;
    }

    public int getId(){return id;}
    public UUID getHash(){return hash;}
    public String getNome(){return nome;}
    public String getDescricao(){return descricao;}
    public String getEan13(){return ean13;}
    public float getPreco(){return preco;}
    public int getQuantidade(){return quantidade;}
    public int getEstoquemin(){return estoque_min;}
    public Timestamp getDtcreate(){return dtcreate;}
    public Timestamp getDtupdate(){return dtupdate;}
    public boolean getLativo(){return lativo;}
}

