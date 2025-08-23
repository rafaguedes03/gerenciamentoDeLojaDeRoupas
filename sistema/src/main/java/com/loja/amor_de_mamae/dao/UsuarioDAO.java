package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.Usuario;
import com.loja.amor_de_mamae.util.ConexaoMySQL;
import com.loja.amor_de_mamae.util.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    public Usuario login(String login, String senha) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            String sql = "SELECT * FROM Usuarios WHERE login = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String senhaArmazenada = rs.getString("senha");
                
                // Verificar se a senha está hasheada (BCrypt)
                if (SecurityUtil.isPasswordHashed(senhaArmazenada)) {
                    // Senha hasheada - verificar com BCrypt
                    if (SecurityUtil.checkPassword(senha, senhaArmazenada)) {
                        return criarUsuarioFromResultSet(rs);
                    }
                } else {
                    // Senha em texto puro (migração de sistema antigo)
                    if (senha.equals(senhaArmazenada)) {
                        // Migrar para hash BCrypt
                        migrarParaHash(rs.getInt("id"), senha);
                        return criarUsuarioFromResultSet(rs);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (nome, login, senha, tipo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Criptografar a senha antes de salvar
            String senhaHash = SecurityUtil.hashPassword(usuario.getSenha());
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, senhaHash); // Salvar o hash BCrypt
            stmt.setString(4, usuario.getTipo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nome, login, tipo FROM Usuarios"; // Não selecionar a senha
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setLogin(rs.getString("login"));
                u.setSenha(""); // Não retornar a senha por segurança
                u.setTipo(rs.getString("tipo"));
                usuarios.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public void excluirUsuario(int id) {
        String sql = "DELETE FROM Usuarios WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean loginExiste(String login) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE login = ?";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE Usuarios SET nome = ?, login = ?, tipo = ? WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getTipo());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void atualizarSenha(int id, String novaSenha) {
        String sql = "UPDATE Usuarios SET senha = ? WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String senhaHash = SecurityUtil.hashPassword(novaSenha);
            stmt.setString(1, senhaHash);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Migra uma senha em texto puro para hash BCrypt
     */
    private void migrarParaHash(int usuarioId, String senhaPlaintext) {
        String sql = "UPDATE Usuarios SET senha = ? WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String senhaHash = SecurityUtil.hashPassword(senhaPlaintext);
            stmt.setString(1, senhaHash);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
            
            System.out.println("Senha migrada para hash BCrypt para usuário ID: " + usuarioId);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Cria objeto Usuario a partir do ResultSet
     */
    private Usuario criarUsuarioFromResultSet(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setLogin(rs.getString("login"));
        u.setSenha(rs.getString("senha")); // Para uso interno apenas
        u.setTipo(rs.getString("tipo"));
        return u;
    }
}