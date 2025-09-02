package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private final Connection conn;

    public ClienteDAO(Connection conn) {
        this.conn = conn;
    }

    

    public List<Cliente> buscarClientes(String termo) throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nome LIKE ? OR cpf LIKE ? ORDER BY nome";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + termo + "%");
            stmt.setString(2, "%" + termo + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId_cliente(rs.getInt("id_cliente"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setData_nascimento(rs.getDate("data_nascimento") != null ? 
                        rs.getDate("data_nascimento").toLocalDate() : null);
                    clientes.add(cliente);
                }
            }
        }
        return clientes;
    }

    public Cliente buscarClientePorCpf(String cpf) throws Exception {
        String sql = "SELECT * FROM cliente WHERE cpf = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId_cliente(rs.getInt("id_cliente"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setData_nascimento(rs.getDate("data_nascimento") != null ? 
                        rs.getDate("data_nascimento").toLocalDate() : null);
                    return cliente;
                }
            }
        }
        return null;
    }

    public void inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, cpf, data_nascimento, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setDate(3, cliente.getData_nascimento() != null ? Date.valueOf(cliente.getData_nascimento()) : null);
            stmt.setString(4, cliente.getEmail());
            stmt.executeUpdate();
        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setData_nascimento(rs.getDate("data_nascimento") != null ?
                        rs.getDate("data_nascimento").toLocalDate() : null);
                c.setEmail(rs.getString("email"));
                lista.add(c);
            }
        }
        return lista;
    }

    public void atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, cpf = ?, data_nascimento = ?, email = ? WHERE id_cliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setDate(3, cliente.getData_nascimento() != null ? Date.valueOf(cliente.getData_nascimento()) : null);
            stmt.setString(4, cliente.getEmail());
            stmt.setInt(5, cliente.getId_cliente());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idCliente) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.executeUpdate();
        }
    }
}
