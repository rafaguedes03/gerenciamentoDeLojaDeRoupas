package com.loja.amor_de_mamae.model;

public class Produto {
    private int id_produto;
    private String codigo;
    private String nome;
    private double preco;
    private String tamanho; // Adicionando o campo tamanho
    private int quantidade;
    

    // Construtor salvar 
    public Produto(String nome, String codigo, double preco) {
        this.nome = nome;
        this.codigo = codigo;
        this.preco = preco;
    }

    // Construtor padrão (útil para frameworks e ORMs)
    public Produto() {
    }

    // getters e setters
    public int getIdProduto() {
        return id_produto;
    }
    public void setIdProduto(int id_produto) {
        this.id_produto = id_produto;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
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
