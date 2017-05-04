package br.com.bomfim.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.bomfim.model.Usuario;
import br.com.bomfim.model.enuns.StatusUsuario;
import br.com.bomfim.repository.Usuarios;
import br.com.bomfim.service.exception.EmailUsuarioJaCadastradoException;
import br.com.bomfim.service.exception.ImpossivelExcluirEntidadeException;
import br.com.bomfim.service.exception.SenhaUsuarioJaCadastradoException;

@Service
public class CadastroUsuarioService {

	@Autowired
	private Usuarios usuarios;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		Optional<Usuario> usuarioRetrived = usuarios.findByEmail(usuario.getEmail());
		if (usuarioRetrived.isPresent() && !usuarioRetrived.get().equals(usuario)) {
			throw new EmailUsuarioJaCadastradoException("E-mail já cadastrado");
		}
		if (usuario.isNovo() && StringUtils.isEmpty(usuario.getSenha())) {
			throw new SenhaUsuarioJaCadastradoException("Senha Obrigatória");
		}
		if (usuario.isNovo() || !StringUtils.isEmpty(usuario.getSenha())) {
			usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
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
		if (usuario.getAtivo() == Boolean.TRUE) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar usuario. Pois ele está Ativo.");
		}
		usuarios.delete(usuario);
	}
}
