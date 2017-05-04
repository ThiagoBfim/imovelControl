package br.com.bomfim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bomfim.model.Grupo;

@Repository
public interface Grupos extends JpaRepository<Grupo, Long> {

}
