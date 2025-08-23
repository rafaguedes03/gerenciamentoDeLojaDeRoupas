package com.loja.amor_de_mamae.dao;

public class ProdutoEstoqueDAO {
    private int id_estoque;
    private int id_produto;
    private String nome;
    private String codigo;
    private double preco;
    private String tamanho;
    private int quantidade;

    // Construtor
    public ProdutoEstoqueDAO(int id_estoque, int id_produto, String nome, String codigo, double preco, String tamanho, int quantidade) {
        this.id_estoque = id_estoque;
        this.id_produto = id_produto;
        this.nome = nome;
        this.codigo = codigo;
        this.preco = preco;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
    }

    // Construtor padrão (útil para frameworks e ORMs)
    public ProdutoEstoqueDAO() {
    }

    // metodo para atualizar um produto
    public void atualizar(int id_produto, String nome, String codigo, double preco, String tamanho, int quantidade) {
        this.id_produto = id_produto;
        this.nome = nome;
        this.codigo = codigo;
        this.preco = preco;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
    }
    
    // Getters e Setters
    public int getId_estoque() { return id_estoque; }
    public void setId_estoque(int id_estoque) { this.id_estoque = id_estoque; }
    
    public int getId_produto() { return id_produto; }
    public void setId_produto(int id_produto) { this.id_produto = id_produto; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    
    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }
    
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}