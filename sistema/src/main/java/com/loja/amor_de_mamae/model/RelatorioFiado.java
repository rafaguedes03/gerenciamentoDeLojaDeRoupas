package com.loja.amor_de_mamae.model;

import java.time.LocalDate;

public class RelatorioFiado {
    private String cliente;
    private String cpf;
    private LocalDate dataVenda;
    private double total;
    private int parcelasPagas;
    private int parcelasTotal;
    private double valorPendente;
    private String status;
    
    // Construtores
    public RelatorioFiado() {}
    
    public RelatorioFiado(String cliente, String cpf, LocalDate dataVenda, double total, 
                         int parcelasPagas, int parcelasTotal, double valorPendente, String status) {
        this.cliente = cliente;
        this.cpf = cpf;
        this.dataVenda = dataVenda;
        this.total = total;
        this.parcelasPagas = parcelasPagas;
        this.parcelasTotal = parcelasTotal;
        this.valorPendente = valorPendente;
        this.status = status;
    }
    
    // Getters e Setters
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public LocalDate getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDate dataVenda) { this.dataVenda = dataVenda; }
    
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    public int getParcelasPagas() { return parcelasPagas; }
    public void setParcelasPagas(int parcelasPagas) { this.parcelasPagas = parcelasPagas; }
    
    public int getParcelasTotal() { return parcelasTotal; }
    public void setParcelasTotal(int parcelasTotal) { this.parcelasTotal = parcelasTotal; }
    
    public double getValorPendente() { return valorPendente; }
    public void setValorPendente(double valorPendente) { this.valorPendente = valorPendente; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}