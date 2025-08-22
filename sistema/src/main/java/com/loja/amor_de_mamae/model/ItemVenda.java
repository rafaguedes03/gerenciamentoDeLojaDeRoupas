package com.loja.amor_de_mamae.model;

public class ItemVenda {
    private int id_item_venda;
    private int id_venda;
    private int id_estoque;
    private int quantidade;
    private double preco_unitario;
    private double subtotal;

    // construtor
    public ItemVenda(int id_estoque, String codigo, String nome, int quantidade, double preco_unitario) {
        this.id_estoque = id_estoque;
        this.quantidade = quantidade;
        this.preco_unitario = preco_unitario;
        this.subtotal = quantidade * preco_unitario; // Calcula o subtotal
    }


    public int getId_item_venda() {
        return id_item_venda;
    }
    public void setId_item_venda(int id_item_venda) {
        this.id_item_venda = id_item_venda;
    }
    public int getId_venda() {
        return id_venda;
    }
    public void setId_venda(int id_venda) {
        this.id_venda = id_venda;
    }
    public int getId_estoque() {
        return id_estoque;
    }
    public void setId_estoque(int id_estoque) {
        this.id_estoque = id_estoque;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public double getPreco_unitario() {
        return preco_unitario;
    }
    public void setPreco_unitario(double preco_unitario) {
        this.preco_unitario = preco_unitario;
    }
    public double getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    
}
