package br.com.bomfim.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.bomfim.model.Usuario;

public interface UsuariosQueries {

	public Page<Usuario> filtrar(Usuario filtro, Pageable pageable);

	public Optional<Usuario> retrieveEmailAtivo(String email);

	public List<String> permissoes(Usuario usuario);

	public Usuario buscarComGrupos(Long codigo);
}
