package com.loja.amor_de_mamae;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteConexao {
    public static void main(String[] args) {
        // Configurações do banco
        String url = "jdbc:mysql://localhost:3306/amor_de_mamae"; // substitua pelo nome do seu DB
        String usuario = "root"; // seu usuário MySQL
        String senha = ""; // sua senha MySQL

        try {
            Connection conn = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexão com o banco de dados realizada com sucesso!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados:");
            e.printStackTrace();
        }
    }
}
