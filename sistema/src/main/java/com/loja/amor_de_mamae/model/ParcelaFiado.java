package com.loja.amor_de_mamae.model;

import java.time.LocalDateTime;

public class ParcelaFiado {
    private int id_parcela;
    private int id_fiado;
    private int numero_parcela;
    private double valor_parcela;
    private LocalDateTime data_vencimento;
    private String status; //ENUM('a vencer','vencida','paga') DEFAULT 'a vencer',


    public int getId_parcela() {
        return id_parcela;
    }
    public void setId_parcela(int id_parcela) {
        this.id_parcela = id_parcela;
    }
    public int getId_fiado() {
        return id_fiado;
    }
    public void setId_fiado(int id_fiado) {
        this.id_fiado = id_fiado;
    }
    public int getNumero_parcela() {
        return numero_parcela;
    }
    public void setNumero_parcela(int numero_parcela) {
        this.numero_parcela = numero_parcela;
    }
    public double getValor_parcela() {
        return valor_parcela;
    }
    public void setValor_parcela(double valor_parcela) {
        this.valor_parcela = valor_parcela;
    }
    public LocalDateTime getData_vencimento() {
        return data_vencimento;
    }
    public void setData_vencimento(LocalDateTime data_vencimento) {
        this.data_vencimento = data_vencimento;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    
}
