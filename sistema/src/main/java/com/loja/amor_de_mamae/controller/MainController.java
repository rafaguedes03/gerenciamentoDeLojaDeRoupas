package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.model.Usuario;
import com.loja.amor_de_mamae.dao.CaixaDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class MainController {

    @FXML private Button btnVendas;
    @FXML private Button btnProdutos;
    @FXML private Button btnClientes;
    @FXML private Button btnFiados;
    @FXML private Button btnRelatorios;
    @FXML private Button btnSair;
    
    @FXML private Pane paneConteudoInicial;
    @FXML private Pane paneConteudo;
    @FXML private Text textTitulo;

    private Usuario usuarioLogado;
    private final CaixaDAO caixaDAO = new CaixaDAO();

    @FXML
    public void initialize() {
        // Guarda o conteúdo inicial do painel
        paneConteudoInicial = new Pane();
        paneConteudoInicial.getChildren().addAll(paneConteudo.getChildren());
    }

    // Recebe o usuário logado
    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
        ajustarBotoesPorPerfil();
    }

    // Desabilita botões se for funcionário
    private void ajustarBotoesPorPerfil() {
        if (usuarioLogado != null && "Funcionario".equalsIgnoreCase(usuarioLogado.getTipo())) {
            btnRelatorios.setDisable(true);
            btnFiados.setDisable(true);
        }
    }

    

    // 🔹 Funções de navegação
    @FXML
    private void abrirVendas() throws Exception {
        if(caixaDAO.isCaixaAberto()){
            carregarTela("/com/loja/amor_de_mamae/view/Vendas.fxml", "Vendas");
        } else {
            abrirTelaAbrirCaixa();
        }
    }

    @FXML
    private void abrirCadastrarProduto() {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/CadastrarProduto.fxml"));
        Node node = loader.load();

        // Passa o usuário logado
        CadastrarProdutoController controller = loader.getController();
        controller.setUsuario(usuarioLogado);

        paneConteudo.getChildren().setAll(node);
        textTitulo.setText("Produtos");

    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlertaErro("Erro", "Não foi possível abrir a tela de produtos");
    }
    }

    @FXML
    private void abrirCadastrarCliente() {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/CadastrarCliente.fxml"));
        Node node = loader.load();

        // Pega o controller da tela carregada
        CadastrarClienteController controller = loader.getController();
        // Passa o usuário logado
        controller.setUsuario(usuarioLogado);

        // Coloca a tela no paneConteudo
        paneConteudo.getChildren().setAll(node);

        // Atualiza o título
        textTitulo.setText("Clientes");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro", "Não foi possível abrir a tela de clientes");
        }
        }

    @FXML
    private void abrirFiados() {
        carregarTela("/com/loja/amor_de_mamae/view/Fiados.fxml", "Fiados");
    }

    @FXML
    private void abrirRelatorios() {
        carregarTela("/com/loja/amor_de_mamae/view/Relatorios.fxml", "Relatórios");
    }

    @FXML
    private void logout() {
        // Desconecta o usuário e volta para a tela de login
        try {
            Stage stage = (Stage) btnSair.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/login.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Amor de Mamãe - Sistema de Vendas");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro", "Não foi possível fazer logout");
        }
    }

    // Método para abrir a tela de abrir caixa em uma janela modal
    private void abrirTelaAbrirCaixa() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/AbrirCaixa.fxml"));
            Pane pane = loader.load();
            
            // Obtém o controller da tela de caixa
            CaixaController caixaController = loader.getController();
            caixaController.setMainController(this); // Passa a referência deste controller
            
            Stage stage = new Stage();
            stage.setTitle("Abrir Caixa");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.setResizable(false);
            stage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao abrir tela de caixa", e.getMessage());
        }
    }

    // Método público para ser chamado pelo CaixaController após abrir o caixa
    public void carregarTelaVendasAposAbertura() {
        try {
            carregarTela("/com/loja/amor_de_mamae/view/Vendas.fxml", "Vendas");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao carregar tela de vendas", e.getMessage());
        }
    }

    // Em cada método de abrir tela, atualize o título:
    void carregarTela(String caminhoFXML, String titulo) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
        Node node = loader.load();

        // Passar o MainController para o controller carregado, se ele for VendasController
        Object controller = loader.getController();
        if (controller != null && controller instanceof VendasController) {
            ((VendasController) controller).setMainController(this);
        }
        // Adicione outros controllers aqui se precisar

        paneConteudo.getChildren().setAll(node);
        textTitulo.setText(titulo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void voltarParaTelaPrincipalAposFechamento() {
        paneConteudo.getChildren().clear();
        paneConteudo.getChildren().addAll(paneConteudoInicial.getChildren());
        textTitulo.setText("Menu Principal");
    }

    private void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}