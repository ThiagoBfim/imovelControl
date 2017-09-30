package br.com.imovelcontrol.repository;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.InformacaoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Thiago on 29/06/2017.
 */
@Repository
public interface InformacaoPagamentos extends JpaRepository<InformacaoPagamento, Long> {

    List<InformacaoPagamento> findByAluguel(Aluguel codAluguel);
//    Optional<List<InformacaoPagamento>> findByAluguel (Aluguel aluguel);
}
