package com.loja.amor_de_mamae.model;

import java.time.LocalDateTime;

public class Caixa {

    // Atributos de Caixa
    private int id_caixa;
    private LocalDateTime data_abertura;
    private LocalDateTime data_fechamento;
    private double saldo_inicial;
    private double saldo_final;
    private Boolean aberto;

    
    public int getId_caixa() {
        return id_caixa;
    }
    public void setId_caixa(int id_caixa) {
        this.id_caixa = id_caixa;
    }
    public LocalDateTime getData_abertura() {
        return data_abertura;
    }
    public void setData_abertura(LocalDateTime data_abertura) {
        this.data_abertura = data_abertura;
    }
    public LocalDateTime getData_fechamento() {
        return data_fechamento;
    }
    public void setData_fechamento(LocalDateTime data_fechamento) {
        this.data_fechamento = data_fechamento;
    }
    public double getSaldo_inicial() {
        return saldo_inicial;
    }
    public void setSaldo_inicial(double saldo_inicial) {
        this.saldo_inicial = saldo_inicial;
    }
    public double getSaldo_final() {
        return saldo_final;
    }
    public void setSaldo_final(double saldo_final) {
        this.saldo_final = saldo_final;
    }
    public Boolean getAberto() {
        return aberto;
    }
    public void setAberto(Boolean aberto) {
        this.aberto = aberto;
    }

    
}