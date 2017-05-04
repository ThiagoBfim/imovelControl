package br.com.bomfim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bomfim.model.tipoimovel.Aluguel;
import br.com.bomfim.repository.helper.aluguel.AlugueisQuerys;

@Repository
public interface Alugueis extends JpaRepository<Aluguel, Long>, AlugueisQuerys {

}
