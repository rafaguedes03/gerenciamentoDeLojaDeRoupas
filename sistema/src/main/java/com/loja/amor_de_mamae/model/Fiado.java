package com.loja.amor_de_mamae.model;

import java.time.LocalDate;

public class Fiado {
    private int id_fiado;
    private int id_venda;
    private String status;
    private LocalDate dataVenda;
    private String nomeCliente;
    private double totalVenda;
    private int numParcelas;
    private int parcelasPagas;
    
    // Construtores
    public Fiado() {}
    
    public Fiado(int id_venda, String status) {
        this.id_venda = id_venda;
        this.status = status;
    }
    
    // Getters e Setters
    public int getId_fiado() { return id_fiado; }
    public void setId_fiado(int id_fiado) { this.id_fiado = id_fiado; }
    
    public int getId_venda() { return id_venda; }
    public void setId_venda(int id_venda) { this.id_venda = id_venda; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDate dataVenda) { this.dataVenda = dataVenda; }
    
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    
    public double getTotalVenda() { return totalVenda; }
    public void setTotalVenda(double totalVenda) { this.totalVenda = totalVenda; }
    
    public int getNumParcelas() { return numParcelas; }
    public void setNumParcelas(int numParcelas) { this.numParcelas = numParcelas; }
    
    public int getParcelasPagas() { return parcelasPagas; }
    public void setParcelasPagas(int parcelasPagas) { this.parcelasPagas = parcelasPagas; }
}