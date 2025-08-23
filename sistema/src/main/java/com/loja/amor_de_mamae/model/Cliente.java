package com.loja.amor_de_mamae.model;

import java.time.LocalDate;

public class Cliente {
    private int id_cliente;
    private String nome;
    private String email;
    private String cpf;
    private LocalDate data_nascimento;

    // Construtor vazio
    public Cliente() {
        // Necessário para você poder instanciar sem argumentos
    }

    // Construtor
    public Cliente(int id_cliente, String nome, String cpf, String email, LocalDate data_nascimento) {
        this.id_cliente = id_cliente;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.data_nascimento = data_nascimento;
    }

    public int getId_cliente() {
        return id_cliente;
    }
    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getData_nascimento() {
        return data_nascimento;
    }
    public void setData_nascimento(LocalDate data_nascimento) {
        this.data_nascimento = data_nascimento;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    
}
