package br.com.imovelcontrol.service;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.mail.EmailSenderConfigure;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.model.enuns.StatusUsuario;
import br.com.imovelcontrol.repository.Usuarios;
import br.com.imovelcontrol.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class CadastroUsuarioService {

    @Autowired
    private Usuarios usuarios;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSenderConfigure emailSenderConfigure;

    @Autowired
    CadastroImovelService cadastroImovelService;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        Optional<Usuario> usuarioRetrived = usuarios.findByEmail(usuario.getEmail());
        if (usuarioRetrived.isPresent() && !usuarioRetrived.get().equals(usuario)) {
            throw new BusinessException("E-mail já cadastrado", "email");
        }
        usuarioRetrived = usuarios.findByLogin(usuario.getLogin());
        if (usuarioRetrived.isPresent() && !usuarioRetrived.get().equals(usuario)) {
            throw new BusinessException("Login já cadastrado", "login");
        }
        if (usuario.isNovo() && StringUtils.isEmpty(usuario.getSenha())) {
            throw new BusinessException("Senha Obrigatória", "Senha");
        }
        if (usuario.isNovo() || !StringUtils.isEmpty(usuario.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        } else if (StringUtils.isEmpty(usuario.getSenha())) {
            usuario.setSenha(usuarioRetrived.get().getSenha());
        }

        if (!usuario.isNovo() && usuarioRetrived.isPresent() && usuario.getAtivo() == null) {
            usuario.setAtivo(usuarioRetrived.get().getAtivo());
        }
        usuario.setConfirmacaoSenha(usuario.getSenha());
        return usuarios.saveAndFlush(usuario);
    }

    @Transactional
    public void alterarStatus(Long[] codigos, StatusUsuario statusUsuario) {
        statusUsuario.executar(codigos, usuarios);
    }

    @Transactional
    public void excluir(Usuario usuario) {
        List<Imovel> imovels = cadastroImovelService.findByDonoImovel(usuario.getCodigo());

        for (Imovel item : imovels) {
            cadastroImovelService.excluirLogicamente(item);
        }
        usuario.setAtivo(Boolean.FALSE);
        usuarios.save(usuario);
    }


    @Transactional
    public Usuario findByLogin(String login) {
        Optional<Usuario> usuario = usuarios.findByLogin(login);
        if (!usuario.isPresent()) {
            throw new BusinessException("Nome de usuário não encontrado no sistema", "login");
        } else {
            return usuario.get();
        }
    }

    @Transactional
    public void enviarNovaSenha(Usuario usuario) {
        String senha = generateSenha(usuario);
        usuario.setSenha(this.passwordEncoder.encode(senha));
        usuarios.save(usuario);
        emailSenderConfigure.setDestinarario(usuario.getEmail());
        emailSenderConfigure.setTitulo("ImovelControl - Recuperar Senha");
        String message = " A sua senha foi alterada com sucesso.\n"
                + " Nova Senha: " + senha;
        emailSenderConfigure.setMensagem(message);
        emailSenderConfigure.enviar();

    }

    /**
     * Método para gerar uma senha aleatoria.
     *
     * @param usuario usuario que terá a senah modificada.
     * @return Senha modificada.
     */
    private String generateSenha(Usuario usuario) {
        int valor = (int) (999 - usuario.getCodigo() + (Math.random() * 99));
        String senha = usuario.getLogin().substring(0, usuario.getLogin().length() / 2);
        senha += ((usuario.getCodigo() + valor) * valor);
        return senha;
    }
}
