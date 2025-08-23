package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.CaixaDAO;
import com.loja.amor_de_mamae.model.Caixa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class CaixaController {

    @FXML private TextField inputSaldoInicial;
    @FXML private Button btnAbrir;

    private MainController mainController;

    // Método para receber a referência do MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void abrirCaixa() {
        try {
            double saldoInicial = Double.parseDouble(inputSaldoInicial.getText());

            Caixa caixa = new Caixa();
            caixa.setData_abertura(LocalDateTime.now());
            caixa.setSaldo_inicial(saldoInicial);
            caixa.setAberto(true);

            CaixaDAO dao = new CaixaDAO();
            dao.abrirCaixa(caixa);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Caixa aberto!");
            alert.setContentText("O caixa foi aberto com sucesso.");
            alert.showAndWait();

            // Após abrir o caixa, carrega a tela de vendas
            if (mainController != null) {
                mainController.carregarTela("/com/loja/amor_de_mamae/view/Vendas.fxml");
            }

            // Fecha a janela atual (se for uma janela separada)
            fecharJanela();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Saldo inválido");
            alert.setContentText("Digite um valor numérico válido para o saldo inicial.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao abrir caixa");
            alert.setContentText("Ocorreu um erro ao abrir o caixa: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) inputSaldoInicial.getScene().getWindow();
        stage.close();
    }
}