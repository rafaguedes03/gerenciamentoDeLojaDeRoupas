package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.CaixaDAO;
import com.loja.amor_de_mamae.model.Caixa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class FecharCaixaController {

    @FXML private TextField inputSaldoFinal;

    private Caixa caixaAtual;
    private MainController mainController;

    public void setCaixaAtual(Caixa caixa) {
        this.caixaAtual = caixa;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void fecharCaixa() {
        try {
            if (caixaAtual == null) {
                mostrarAlerta("Erro", "Caixa não encontrado!", Alert.AlertType.ERROR);
                return;
            }

            double saldoFinal = Double.parseDouble(inputSaldoFinal.getText());

            // Atualizar dados do caixa
            caixaAtual.setData_fechamento(LocalDateTime.now());
            caixaAtual.setSaldo_final(saldoFinal);
            caixaAtual.setAberto(false);

            // Calcular o valor das vendas (saldo final - saldo inicial)
            double valorVendas = saldoFinal - caixaAtual.getSaldo_inicial();
            caixaAtual.setValor_vendas(valorVendas);

            // Fechar o caixa no banco
            CaixaDAO dao = new CaixaDAO();
            dao.fecharCaixa(caixaAtual);

            mostrarAlerta("Sucesso", "Caixa fechado com sucesso!", Alert.AlertType.INFORMATION);

            // Fechar a janela
            fecharJanela();

            // Voltar para a tela principal ou outra tela
            if (mainController != null) {
                mainController.voltarParaTelaPrincipalAposFechamento();
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Digite um valor válido para o saldo final!", Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao fechar caixa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) inputSaldoFinal.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}