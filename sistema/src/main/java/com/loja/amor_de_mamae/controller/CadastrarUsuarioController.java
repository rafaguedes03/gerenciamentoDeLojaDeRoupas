package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.UsuarioDAO;
import com.loja.amor_de_mamae.model.Usuario;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class CadastrarUsuarioController implements Initializable {

    @FXML private TextField inputNome;
    @FXML private TextField inputLogin;
    @FXML private PasswordField inputSenha;
    @FXML private PasswordField inputConfirmarSenha;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TableView<Usuario> tableView;
    @FXML private TableColumn<Usuario, String> colunaNome;
    @FXML private TableColumn<Usuario, String> colunaLogin;
    @FXML private TableColumn<Usuario, String> colunaTipo;
    @FXML private TableColumn<Usuario, String> colunaAcao;
    
    private UsuarioDAO usuarioDAO;
    private ObservableList<Usuario> usuariosList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuarioDAO = new UsuarioDAO();
        usuariosList = FXCollections.observableArrayList();
        
        // Configurar combobox de tipos
        comboTipo.getItems().addAll("Administrador", "Funcionário");
        comboTipo.setValue("Funcionário");
        
        // Configurar tabela
        configurarTabela();
        
        // Carregar usuários
        carregarUsuarios();
    }
    
    private void configurarTabela() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        
        // Configurar coluna de ação (editar/excluir)
        colunaAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            
            {
                btnEditar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
                btnExcluir.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                
                btnEditar.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    editarUsuario(usuario);
                });
                
                btnExcluir.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    excluirUsuario(usuario);
                });
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox botoes = new HBox(5, btnEditar, btnExcluir);
                    setGraphic(botoes);
                }
            }
        });
        
        tableView.setItems(usuariosList);
    }
    
    private void carregarUsuarios() {
        // Implementar método para carregar usuários do banco
        usuariosList.clear();
        usuariosList.addAll(usuarioDAO.listarTodos());
        tableView.refresh();
    }
    
    @FXML
    private void cadastrarUsuario() {
        if (validarCampos()) {
            Usuario usuario = new Usuario();
            usuario.setNome(inputNome.getText());
            usuario.setLogin(inputLogin.getText());
            usuario.setSenha(inputSenha.getText());
            usuario.setTipo(comboTipo.getValue());
            
            usuarioDAO.cadastrarUsuario(usuario);
            limparCampos();
            carregarUsuarios(); // Recarregar tabela
            
            // Mostrar mensagem de sucesso
            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setTitle("Sucesso");
            sucesso.setHeaderText(null);
            sucesso.setContentText("Usuário cadastrado com sucesso!");
            sucesso.showAndWait();
        }
    }

    // Ao clicar em Login ir para a tela de login
    public void login() {
        try {
            Stage stage = (Stage) inputNome.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/Login.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean validarCampos() {
        if (inputNome.getText().isEmpty() || inputLogin.getText().isEmpty() || 
            inputSenha.getText().isEmpty() || inputConfirmarSenha.getText().isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!");
            return false;
        }
        
        if (!inputSenha.getText().equals(inputConfirmarSenha.getText())) {
            mostrarAlerta("Erro", "As senhas não coincidem!");
            return false;
        }
        
        // Verificar se login já existe (implementar no DAO)
        // if (usuarioDAO.loginExiste(inputLogin.getText())) {
        //     mostrarAlerta("Erro", "Login já existe!");
        //     return false;
        // }
        
        return true;
    }
    
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    private void limparCampos() {
        inputNome.clear();
        inputLogin.clear();
        inputSenha.clear();
        inputConfirmarSenha.clear();
        comboTipo.setValue("Funcionário");
    }
    
    @FXML
    private void cancelarEdicao() {
        limparCampos();
    }
    
    private void editarUsuario(Usuario usuario) {
        inputNome.setText(usuario.getNome());
        inputLogin.setText(usuario.getLogin());
        comboTipo.setValue(usuario.getTipo());
        // TODO: Implementar lógica completa de edição
    }
    
    // ecluir usuário
    private void excluirUsuario(Usuario usuario) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação de Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Tem certeza que deseja excluir o usuário " + usuario.getNome() + "?");
        
        if (confirmacao.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            usuarioDAO.excluirUsuario(usuario.getId());
            carregarUsuarios(); // Recarregar tabela
            
            // Mostrar mensagem de sucesso
            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setTitle("Sucesso");
            sucesso.setHeaderText(null);
            sucesso.setContentText("Usuário excluído com sucesso!");
            sucesso.showAndWait();
        }
    }
}