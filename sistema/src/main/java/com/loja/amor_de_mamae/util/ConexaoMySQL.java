package com.loja.amor_de_mamae.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoMySQL {
    private static final String URL = "jdbc:mysql://localhost:3306/amor_de_mamae";
    private static final String USER = "root"; 
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro na conexão com o banco de dados");
        }
    }
}
