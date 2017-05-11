package br.com.imovelcontrol.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuariosQueries {

	public Page<Usuario> filtrar(Usuario filtro, Pageable pageable);

	public Optional<Usuario> retrieveEmailAtivo(String email);

	public List<String> permissoes(Usuario usuario);

	public Usuario buscarComGrupos(Long codigo);
}
