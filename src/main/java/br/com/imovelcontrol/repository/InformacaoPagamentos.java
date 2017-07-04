package br.com.imovelcontrol.repository;

import java.util.Optional;

import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Usuario on 29/06/2017.
 */
@Repository
public interface InformacaoPagamentos extends JpaRepository<InformacaoPagamento, Long> {

    Optional<InformacaoPagamento> findByAluguel(Aluguel codAluguel);
}
