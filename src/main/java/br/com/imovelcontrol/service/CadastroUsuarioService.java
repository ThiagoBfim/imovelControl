package br.com.imovelcontrol.service;

import java.time.LocalDate;
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

        if (usuario.getDataNascimento() != null
                && usuario.getDataNascimento().isAfter(LocalDate.now().minusYears(18))) {
            throw new BusinessException("Idade precisa ser maior que 18 anos", "dataNascimento");
        }
        if (usuario.isNovo() || !StringUtils.isEmpty(usuario.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        } else if (StringUtils.isEmpty(usuario.getSenha()) && usuarioRetrived.isPresent()) {
            usuario.setSenha(usuarioRetrived.get().getSenha());
        }

        if (usuario.getGrupos().isEmpty() && usuarioRetrived.isPresent()) {
            usuario.setGrupos(usuarioRetrived.get().getGrupos());
        }

        if (!usuario.isNovo() && usuarioRetrived.isPresent() && usuario.getAtivo() == null) {
            usuario.setAtivo(usuarioRetrived.get().getAtivo());
        }
        usuario.setConfirmacaoSenha(usuario.getSenha());
        Long usuarioCodigo = usuario.getCodigo();
        Usuario usuarioSession = usuarios.saveAndFlush(usuario);
        if (usuarioCodigo == null) {
            enviarCodigoVerificador(usuario);
        }
        return usuarioSession;
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
    public Usuario findByEmail(String login) {
        Optional<Usuario> usuario = usuarios.findByEmail(login);

        if (!usuario.isPresent()) {
            throw new BusinessException("E-mail do usuário não encontrado no sistema", "login");
        } else {
            return usuario.get();
        }
    }

    @Transactional
    public void enviarNovaSenhaAndActiveConta(Usuario usuario) {
        if (!usuario.getAtivo()) {
            usuario.setAtivo(Boolean.TRUE);
        }
        String senha = generateSenha(usuario);
        usuario.setSenha(this.passwordEncoder.encode(senha));
        usuarios.save(usuario);
        emailSenderConfigure.setDestinarario(usuario.getEmail());
        emailSenderConfigure.setTitulo("ImovelControl - Recuperar Senha");
        String message = "<h2>Senha alterada com sucesso do sistema <font color='#009900\'>Imóvel Control, </font> </h2>"
                + "<br><b> Nova Senha: <i><font color='red\'>" + senha + "</font></i></b>";
        emailSenderConfigure.setMensagem(message);
        emailSenderConfigure.enviar();

    }

    /**
     * Método para gerar uma senha aleatoria.
     *
     * @param usuario usuario que terá a senha modificada.
     * @return Senha modificada.
     */
    private String generateSenha(Usuario usuario) {
        int valor = (int) (999 - usuario.getCodigo() + (Math.random() * 99));
        String senha = usuario.getLogin().substring(0, usuario.getLogin().length() / 2);
        senha += ((usuario.getCodigo() + valor) * valor);
        return senha;
    }

    private void enviarCodigoVerificador(Usuario usuario) {
        emailSenderConfigure.setDestinarario(usuario.getEmail());
        emailSenderConfigure.setTitulo("ImovelControl - Seja Bem Vindo.");
        String message = "<h2>Sejá bem vindo ao sistema <font color='#009900\'>Imóvel Control, </font> </h2>"
                + "<h3>O seu sistema de gerenciamento de imóveis.</h3>"
                + "<br>Sua conta foi cadastrada com sucesso."
                + "<br><b>Seu código verificador é:  " + usuario.getCodigoVerificador() + "</b> "
                + "<br><i><font color='red\'>Esse código não é alterado, favor guardar em segurança. </font></i>";
        emailSenderConfigure.setMensagem(message);
        emailSenderConfigure.enviar();
    }
}
