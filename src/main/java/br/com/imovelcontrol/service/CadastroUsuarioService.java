package br.com.imovelcontrol.service;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.model.enuns.StatusUsuario;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.repository.Usuarios;
import br.com.imovelcontrol.service.exception.EmailUsuarioJaCadastradoException;
import br.com.imovelcontrol.service.exception.ImpossivelExcluirEntidadeException;
import br.com.imovelcontrol.service.exception.LoginUsuarioNaoEncontradoException;
import br.com.imovelcontrol.service.exception.SenhaUsuarioJaCadastradoException;
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
	Imoveis imoveis;

	@Autowired
	CadastroImovelService cadastroImovelService;

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
		Optional<List<Imovel>> imovels;
		imovels = imoveis.findByDonoImovel_Codigo(usuario.getCodigo());
		if (usuario.getAtivo() == Boolean.TRUE) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar usuario. Pois ele está Ativo.");
		}
		if(imovels.isPresent()){
			for (Imovel item :
					imovels.get()) {
				cadastroImovelService.excluir(item);
			}
		}
		usuarios.delete(usuario);
	}

	@Transactional
	public Usuario findByEmail(String email){
		Usuario usuario = new Usuario();
		if(!usuarios.findByEmail(email).isPresent()){
			throw new EmailUsuarioJaCadastradoException("O email não está cadastrado no sistema");
		}
		return usuarios.findByEmail(email).get();
	}

	@Transactional
	public Usuario findByLogin(String login){

		if(!usuarios.findByLogin(login).isPresent()){
			throw new LoginUsuarioNaoEncontradoException("Nome de usuário não encontrado no sistema");
		}
		return usuarios.findByLogin(login).get();
	}
}
