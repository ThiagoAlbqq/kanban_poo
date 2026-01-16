package controller.usuario;

import models.KanbanModel;
import models.UsuarioEntity; // Ajuste o import conforme sua entidade
import view.usuario.EditarUsuarioView;
import view.Observer;

public class EditarUsuarioController implements Observer {

    private KanbanModel model;
    private EditarUsuarioView view;

    private String novoNome, novoEmail, novaSenha;

    public void init(KanbanModel model, EditarUsuarioView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            model.attachObserver(this);
        }
    }

    public void editar() {
        String[] usuarioAtual = model.getUsuarioLogadoString();

        view.message("--- EDITAR PERFIL (Deixe vazio para manter) ---");

        // --- NOME ---
        view.message("Nome atual: " + usuarioAtual[1]);
        view.solicitarNome();
        this.novoNome = view.getNome();


        // --- EMAIL ---
        view.message("Email atual: " + usuarioAtual[2]);
        view.solicitarEmail();
        this.novoEmail = view.getEmail();

        // --- SENHA ---
        view.message("Senha (min 6 digitos):");
        view.solicitarSenha();

        // Loop de validação: Só entra se a pessoa DIGITOU algo e for menor que 6
        while (!view.getSenha().isEmpty() && view.getSenha().length() < 6) {
            view.failMensage("A senha é muito curta! Tente novamente ou deixe vazio.");
            view.solicitarSenha();
        }
        this.novaSenha = view.getSenha(); // Usa a nova

        try {
            // Chama o model com os dados definidos acima
            model.editarUsuario(novoNome, novoEmail, novaSenha);

            view.sucessMensage("Edição efetuada com sucesso!");

        } catch (RuntimeException e) {
            view.failMensage("Erro: " + e.getMessage());
        }
    }

    @Override
    public void update() {}
}