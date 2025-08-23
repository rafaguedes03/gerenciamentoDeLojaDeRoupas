package com.loja.amor_de_mamae.model;

import java.time.LocalDateTime;

public class Vendas {
    private int id_venda;
    private int id_cliente;
    private int id_caixa;
    private LocalDateTime data_venda;
    private double total_venda;
    private String forma_pagamento;
    private String status_parcelado;
    private int num_parcelas;
    
    // Construtores
    public Vendas() {}
    
    public Vendas(int id_cliente, int id_caixa, double total_venda, String forma_pagamento) {
        this.id_cliente = id_cliente;
        this.id_caixa = id_caixa;
        this.total_venda = total_venda;
        this.forma_pagamento = forma_pagamento;
        this.data_venda = LocalDateTime.now();
        this.status_parcelado = "nao";
        this.num_parcelas = 1;
    }
    
    // Getters e Setters
    public int getId_venda() { return id_venda; }
    public void setId_venda(int id_venda) { this.id_venda = id_venda; }
    
    public int getId_cliente() { return id_cliente; }
    public void setId_cliente(int id_cliente) { this.id_cliente = id_cliente; }
    
    public int getId_caixa() { return id_caixa; }
    public void setId_caixa(int id_caixa) { this.id_caixa = id_caixa; }
    
    public LocalDateTime getData_venda() { return data_venda; }
    public void setData_venda(LocalDateTime data_venda) { this.data_venda = data_venda; }
    
    public double getTotal_venda() { return total_venda; }
    public void setTotal_venda(double total_venda) { this.total_venda = total_venda; }
    
    public String getForma_pagamento() { return forma_pagamento; }
    public void setForma_pagamento(String forma_pagamento) { this.forma_pagamento = forma_pagamento; }
    
    public String getStatus_parcelado() { return status_parcelado; }
    public void setStatus_parcelado(String status_parcelado) { this.status_parcelado = status_parcelado; }
    
    public int getNum_parcelas() { return num_parcelas; }
    public void setNum_parcelas(int num_parcelas) { this.num_parcelas = num_parcelas; }
}