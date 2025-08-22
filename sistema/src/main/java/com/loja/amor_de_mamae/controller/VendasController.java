package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.CaixaDAO;
import com.loja.amor_de_mamae.dao.VendasDAO;
import com.loja.amor_de_mamae.model.Caixa;
import com.loja.amor_de_mamae.model.Vendas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VendasController {

    @FXML
    private Button btnFinalizarVenda;

    @FXML
    private Button btnFecharCaixa;

    @FXML
    private ChoiceBox<String> formaDePagamento;

    @FXML
    private ChoiceBox<Integer> numParcelas;

    @FXML
    private TextField exibitTotal;

    private final CaixaDAO caixaDAO = new CaixaDAO();
    private final VendasDAO vendasDAO = new VendasDAO();

    private Caixa caixaAtual;

    @FXML
    public void initialize() {
        try {
            boolean caixaAberto = caixaDAO.isCaixaAberto();
            if (!caixaAberto) {
                abrirTelaAbrirCaixa();
            } else {
                habilitarVendas();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível verificar o estado do caixa.");
        }
    }

    // ========================
    // AÇÕES DE VENDA
    // ========================
    @FXML
    private void registrarVenda(ActionEvent event) {
        try {
            Vendas venda = new Vendas();
            venda.setData_venda(LocalDateTime.now());
            venda.setId_caixa(caixaAtual.getId_caixa());
            venda.setTotal_venda(new BigDecimal(exibitTotal.getText()));
            venda.setForma_pagamento(formaDePagamento.getValue());
            venda.setNum_parcelas(numParcelas.getValue() != null ? numParcelas.getValue() : 1);

            vendasDAO.registrarVenda(venda);

            mostrarAlerta("Sucesso", "Venda registrada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao registrar venda.");
        }
    }

    // ========================
    // AÇÕES DO CAIXA
    // ========================
    @FXML
    private void fecharCaixa(ActionEvent event) {
        try {
            caixaAtual.setData_fechamento(LocalDateTime.now());
            caixaAtual.setSaldo_final(Double.parseDouble(exibitTotal.getText())); // aqui você pode calcular o saldo real

            caixaDAO.fecharCaixa(caixaAtual.getId_caixa(), caixaAtual);

            mostrarAlerta("Caixa", "Caixa fechado com sucesso!");
            btnFinalizarVenda.setDisable(true);
            btnFecharCaixa.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao fechar o caixa.");
        }
    }

    private void abrirTelaAbrirCaixa() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/AbrirCaixa.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Abrir Caixa");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Após fechar a tela, verifica novamente
            if (caixaDAO.isCaixaAberto()) {
                habilitarVendas();
            } else {
                mostrarAlerta("Aviso", "O caixa não foi aberto. As vendas não estão disponíveis.");
                btnFinalizarVenda.setDisable(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível abrir a tela de Abrir Caixa.");
        }
    }

    private void habilitarVendas() {
        btnFinalizarVenda.setDisable(false);
        btnFecharCaixa.setDisable(false);
    }

    // ========================
    // UTILITÁRIOS
    // ========================
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
