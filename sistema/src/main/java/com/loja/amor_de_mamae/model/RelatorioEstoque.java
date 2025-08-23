package com.loja.amor_de_mamae.model;

public class RelatorioEstoque {
    private String produto;
    private String codigo;
    private String tamanho;
    private int quantidade;
    private double valorTotal;
    
    // Construtores
    public RelatorioEstoque() {}
    
    public RelatorioEstoque(String produto, String codigo, String tamanho, int quantidade, double valorTotal) {
        this.produto = produto;
        this.codigo = codigo;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }
    
    // Getters e Setters
    public String getProduto() { return produto; }
    public void setProduto(String produto) { this.produto = produto; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }
    
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
}