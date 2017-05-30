package br.com.imovelcontrol.repository;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.repository.helper.usuario.UsuariosQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Usuarios extends JpaRepository<Usuario, Long>, UsuariosQueries {

	Optional<Usuario> findByEmail(String email);
	
	List<Usuario> findByCodigoIn(Long[] codigos);

    Optional<Usuario> findByLogin(String login);
}
