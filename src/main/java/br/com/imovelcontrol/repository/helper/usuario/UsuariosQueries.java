package br.com.imovelcontrol.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuariosQueries {

    Page<Usuario> filtrar(Usuario filtro, Pageable pageable);

    Optional<Usuario> retrieveLoginAtivo(String email);

    List<String> permissoes(Usuario usuario);

    Usuario buscarComGrupos(Long codigo);
}
