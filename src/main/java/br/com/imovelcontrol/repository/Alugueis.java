package br.com.imovelcontrol.repository;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.helper.aluguel.AlugueisQuerys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Alugueis extends JpaRepository<Aluguel, Long>, AlugueisQuerys {

    Optional<Aluguel> findByNome(String nome);
    Optional<List<Aluguel>>findByImovel_Codigo(Long codigo);

}
