package br.com.bomfim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bomfim.model.Imovel;
import br.com.bomfim.repository.helper.imovel.ImoveisQuerys;

@Repository
public interface Imoveis extends JpaRepository<Imovel, Long>, ImoveisQuerys {

}
