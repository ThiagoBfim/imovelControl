package br.com.imovelcontrol.repository;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.repository.helper.informacaopagamento.InformacoesPagamentosQuerys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Thiago on 29/06/2017.
 */
@Repository
public interface InformacoesPagamentos extends JpaRepository<InformacaoPagamento, Long>, InformacoesPagamentosQuerys {

    Optional<List<InformacaoPagamento>> findByAluguel(Aluguel codAluguel);
}
