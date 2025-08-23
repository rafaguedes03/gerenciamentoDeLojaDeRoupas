package com.loja.amor_de_mamae.model;

import java.time.LocalDate;

public class RelatorioVenda {
    private LocalDate data;
    private String descricao;
    private double valor;
    private int quantidade;
    private String formaPagamento;
    
    // Construtores
    public RelatorioVenda() {}
    
    public RelatorioVenda(LocalDate data, String descricao, double valor, int quantidade, String formaPagamento) {
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
        this.quantidade = quantidade;
        this.formaPagamento = formaPagamento;
    }
    
    // Getters e Setters
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    
    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
}