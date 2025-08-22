package com.loja.amor_de_mamae.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Vendas {
    private int id_venda;
    private Integer id_cliente;
    private int id_caixa;
    private LocalDateTime data_venda;
    private BigDecimal total_venda;
    private String forma_pagamento;
    private String status_parcelado;
    private int num_parcelas;

    // metodo para registrar venda
    public void registraVenda(int id_cliente, int id_caixa, LocalDateTime data_venda, BigDecimal total_venda, int num_parcelas) {
        this.id_cliente = id_cliente;
        this.id_caixa = id_caixa;
        this.data_venda = data_venda;
        this.total_venda = total_venda;
        this.num_parcelas = num_parcelas;
        this.forma_pagamento = "Dinheiro"; // valor default
        this.status_parcelado = "nao"; // valor default
    }

    // construtor
    public Vendas(int id_cliente, int id_caixa, LocalDateTime data_venda, BigDecimal total_venda, String forma_pagamento, String status_parcelado, int num_parcelas) {
        this.id_cliente = id_cliente;
        this.id_caixa = id_caixa;
        this.data_venda = data_venda;
        this.total_venda = total_venda;
        this.forma_pagamento = forma_pagamento;
        this.status_parcelado = status_parcelado;
        this.num_parcelas = num_parcelas;
    }

    public Vendas() {
    // Construtor vazio (necessário para você poder instanciar sem argumentos)
    }
    

     // Getters and Setters
    public int getId_venda() {
        return id_venda;
    }
    public void setId_venda(int id_venda) {
        this.id_venda = id_venda;
    }
    public Integer getId_cliente() {
        return id_cliente;
    }
    public void setId_cliente(Integer id_cliente) {
        this.id_cliente = id_cliente;
    }
    public int getId_caixa() {
        return id_caixa;
    }
    public void setId_caixa(int id_caixa) {
        this.id_caixa = id_caixa;
    }
    public LocalDateTime getData_venda() {
        return data_venda;
    }
    public void setData_venda(LocalDateTime data_venda) {
        this.data_venda = data_venda;
    }
    public BigDecimal getTotal_venda() {
        return total_venda;
    }
    public void setTotal_venda(BigDecimal total_venda) {
        this.total_venda = total_venda;
    }
    public int getNum_parcelas() {
        return num_parcelas;
    }
    public void setNum_parcelas(int num_parcelas) {
        this.num_parcelas = num_parcelas;
    }
    public String getForma_pagamento() {
        return forma_pagamento;
    }
    public void setForma_pagamento(String forma_pagamento) {
        this.forma_pagamento = forma_pagamento;
    }
    public String getStatus_parcelado() {
        return status_parcelado;
    }
    public void setStatus_parcelado(String status_parcelado) {
        this.status_parcelado = status_parcelado;
    }

   
    
}
