package com.loja.amor_de_mamae.model;

import java.time.LocalDateTime;

public class Caixa {
    private int id_caixa;
    private LocalDateTime data_abertura;
    private LocalDateTime data_fechamento;
    private double saldo_inicial;
    private double saldo_final;
    private double valor_vendas;
    private boolean aberto;

    // Getters e Setters
    public int getId_caixa() { return id_caixa; }
    public void setId_caixa(int id_caixa) { this.id_caixa = id_caixa; }
    
    public LocalDateTime getData_abertura() { return data_abertura; }
    public void setData_abertura(LocalDateTime data_abertura) { this.data_abertura = data_abertura; }
    
    public LocalDateTime getData_fechamento() { return data_fechamento; }
    public void setData_fechamento(LocalDateTime data_fechamento) { this.data_fechamento = data_fechamento; }
    
    public double getSaldo_inicial() { return saldo_inicial; }
    public void setSaldo_inicial(double saldo_inicial) { this.saldo_inicial = saldo_inicial; }
    
    public double getSaldo_final() { return saldo_final; }
    public void setSaldo_final(double saldo_final) { this.saldo_final = saldo_final; }
    
    public double getValor_vendas() { return valor_vendas; }
    public void setValor_vendas(double valor_vendas) { this.valor_vendas = valor_vendas; }
    
    public boolean isAberto() { return aberto; }
    public void setAberto(boolean aberto) { this.aberto = aberto; }
}