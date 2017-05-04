package br.com.bomfim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bomfim.model.Usuario;
import br.com.bomfim.repository.helper.usuario.UsuariosQueries;

@Repository
public interface Usuarios extends JpaRepository<Usuario, Long>, UsuariosQueries {

	Optional<Usuario> findByEmail(String email);
	
	List<Usuario> findByCodigoIn(Long[] codigos);

}
