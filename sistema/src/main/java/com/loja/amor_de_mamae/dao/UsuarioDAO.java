package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.Usuario;
import java.sql.*;

public class UsuarioDAO {
    public Usuario login(String login, String senha) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            String sql = "SELECT * FROM Usuarios WHERE login=? AND senha=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setTipo(rs.getString("tipo"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
