package br.com.imovelcontrol.repository;

import br.com.imovelcontrol.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Grupos extends JpaRepository<Grupo, Long> {

}
