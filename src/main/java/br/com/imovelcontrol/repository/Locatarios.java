package br.com.imovelcontrol.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Locatarios extends JpaRepository<Locatario, Long> {

    Optional<Locatario> findByAluguelAndExcluido(Aluguel aluguel, Boolean aFalse);

    List<Locatario> findByDataFimGreaterThanAndAluguel(LocalDate dataInicio, Aluguel aluguel);

    List<Locatario> findByDataInicioGreaterThanEqualAndAluguel(LocalDate dataInicio, Aluguel aluguel);
}
