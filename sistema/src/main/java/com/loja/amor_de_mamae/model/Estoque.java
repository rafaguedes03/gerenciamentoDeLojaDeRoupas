package com.loja.amor_de_mamae.model;

public class Estoque {
    
    private int idEstoque;
    private int idProduto;
    private String tamanho;
    private int quantidade;

    // Construtor padrão (útil para frameworks e ORMs)
    public Estoque() {
    }

    // Construtor para criar um novo item de estoque antes de salvar no DB
    public Estoque(int idProduto, String tamanho, int quantidade) {
        this.idProduto = idProduto;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    public int getIdEstoque() {
        return idEstoque;
    }

    public void setIdEstoque(int idEstoque) {
        this.idEstoque = idEstoque;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}